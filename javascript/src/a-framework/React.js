/**
 * 组件
 */
class Component {
    constructor() {
        this.__id = null
        this.__view = null
        this.__parent = null
        this.__viewAttached = null
    }

    __onViewAttached() {
    }

    __onViewDetached() {
    }

    $(id) {
        return this.findViewById(id)
    }

    findViewById(id) {
        return null
    }
}

class PureComponent extends Component {
    constructor(attributes, type) {
        super()
        attributes = attributes || {}
        this.__id = attributes.id
        delete attributes.id

        this.__parent = null
        this.__children = []
        this.__bindComponent = null
        this.__view = ui.createElement(type)
        this.__setAttributesToView(attributes)

        // 可视属性要特殊处理
        this.__defineAttribute('visibility', (_, visibility) => {
            if (visibility !== 'none' && this.__parent && this.__parent.__viewAttached) {
                this.__onViewAttached()
            }
        })

        this.__defineProperty("width", "getWidth")
        this.__defineProperty("height", "getHeight")

        this.__defineAttribute('layoutWidth')
        this.__defineAttribute('layoutHeight')

        this.__defineAttribute('paddingLeft')
        this.__defineAttribute('paddingTop')
        this.__defineAttribute('paddingRight')
        this.__defineAttribute('paddingBottom')

        this.__defineAttribute('marginLeft')
        this.__defineAttribute('marginTop')
        this.__defineAttribute('marginRight')
        this.__defineAttribute('marginBottom')

        this.__defineAttribute('backgroundColor')

        this.__defineAttribute('borderWidth')
        this.__defineAttribute('borderColor')
        this.__defineAttribute('borderRadius')

        this.__defineAttribute('onSizeChanged')
        this.__defineAttribute('onClick')
        this.__defineAttribute('onLongClick')
    }

    __defineProperty(propName, getFuncName) {
        Object.defineProperty(this, propName, {
            get() {
                return this.__view[getFuncName]()
            },
            enumerable: false,
            configurable: false
        })
    }

    __defineAttribute(attrName, afterSet = null) {
        Object.defineProperty(this, attrName, {
            get() {
                return this.__view.getAttribute(attrName)
            },
            set(v) {
                if (this.__view.getAttribute(attrName) !== v) {
                    this.__view.setAttribute(attrName, v)
                    if (afterSet) {
                        afterSet(attrName, v)
                    }
                }
            },
            enumerable: false,
            configurable: false
        })
    }

    __setAttributesToView(attributes) {
        if (this.__view) {
            if (attributes.hasOwnProperty('style')) {
                const styles = attributes['style']
                delete attributes['style']
                attributes = Object.assign({}, styles, attributes)
            }
            for (let key in attributes) {
                if (attributes.hasOwnProperty(key)) {
                    this.__view.setAttribute(key, attributes[key])
                }
            }
        }
    }

    __onViewAttached() {
        const visibility = this.visibility === undefined ? 'visible' : this.visibility
        if (visibility !== 'gone' && !this.__viewAttached) {
            this.__viewAttached = true
            for (let child of this.__children) {
                child.__onViewAttached()
            }
            this.onAttach()

            if (this.__bindComponent) {
                this.__bindComponent.onMount()
            }
        }
    }

    __onViewDetached() {
        if (this.__viewAttached) {
            this.__viewAttached = false
            for (let child of this.__children) {
                child.__onViewDetached()
            }
            this.onDetach()

            if (this.__bindComponent) {
                this.__bindComponent.onUnmount()
            }
        }
    }

    onAttach() {

    }

    onDetach() {
        // impl
    }

    findViewById(id) {
        return this.__id === id ? this : null
    }
}

class PureGroupComponent extends PureComponent {
    constructor(attributes, type, children) {
        super(attributes, type)
        this.__childAttributePrototype = {}
        for (let c of children || []) {
            this.addChild(c)
        }
    }

    __defineChildAttribute(attrName) {
        Object.defineProperty(this.__childAttributePrototype, attrName, {
            get() {
                return this.__view.getAttribute(attrName)
            },
            set(v) {
                if (this.__view.getAttribute(attrName) !== v) {
                    this.__view.setAttribute(attrName, v)
                }
            },
            enumerable: false,
            configurable: false
        })
    }

    findViewById(id) {
        const v = super.findViewById(id)
        if (v) {
            return v
        }
        for (let c of this.__children) {
            if (c instanceof DefinedComponent) {
                if (c.__id === id) {
                    return c
                }
            } else {
                const v = c.findViewById(id)
                if (v) {
                    return v
                }
            }
        }
        return null
    }

    addChild(child, index) {
        if ((index !== 0 && !index) || index <= -1) {
            index = this.__children.length
        }
        if (child instanceof DefinedComponent) {
            if (!child.__parent) {
                this.__children.splice(index, 0, child)
                child.__parent = this
                child.__root.prototype = this.__childAttributePrototype
                this.__view.addChild(child.__root.__view, index)
                if (this.__viewAttached) {
                    child.__onViewAttached()
                }
            }
        } else {
            if (!child.__parent) {
                this.__children.splice(index, 0, child)
                child.__parent = this
                child.prototype = this.__childAttributePrototype
                this.__view.addChild(child.__view, index)
                if (this.__viewAttached) {
                    child.__onViewAttached()
                }
            }
        }
    }

    removeChild(child) {
        if (child instanceof DefinedComponent) {
            if (child.__parent === this) {
                const index = this.__children.indexOf(child)
                this.__children.splice(index, 1)
                this.__view.removeChild(child.__root.__view)
                child.__parent = null
                child.__root.prototype = null
                if (this.__viewAttached) {
                    child.__onViewDetached()
                }
            }
        } else {
            if (child.__parent === this) {
                const index = this.__children.indexOf(child)
                this.__children.splice(index, 1)
                this.__view.removeChild(child.__view)
                child.__parent = null
                child.prototype = null
                if (this.__viewAttached) {
                    child.__onViewDetached()
                }
            }
        }
    }

    removeAllViews() {
        const copy = []
        copy.push(this.__children)
        for (let c of copy) {
            this.removeChild(c)
        }
    }
}

class DefinedComponent extends Component {
    constructor(props) {
        super()
        this.props = props || {}
        this.__id = this.props.id
        this.__parent = null

        this.__root = this.render()
        if (this.__root instanceof DefinedComponent) {
            throw new Error("自定义组件的根视图不能是自定义组件")
        }
        this.__root.__parent = this
        this.__root.__bindComponent = this
        this.__view = this.__root.__view

        // 定义visibility属性
        Object.defineProperty(this, 'visibility', {
            get() {
                return this.__root.visibility
            },
            set(v) {
                this.__root.visibility = v
            },
            enumerable: false,
            configurable: false
        })
    }

    onAddSlot(slotComponent, slotName) {
        // impl
    }

    onMount() {
        // impl
    }

    onUnmount() {
        // impl
    }

    findViewById(id) {
        return this.__root.findViewById(id);
    }

    __onViewAttached() {
        this.__viewAttached = true
        this.__root.__onViewAttached();
    }

    __onViewDetached() {
        this.__viewAttached = false
        this.__root.__onViewDetached()
    }
}

class ViewComponent extends PureComponent {
    constructor(attributes) {
        super(attributes, 'view')
    }
}

class TextComponent extends PureComponent {
    constructor(attributes) {
        super(attributes, 'text')
        this.__defineAttribute('text')
        this.__defineAttribute('gravity')
        this.__defineAttribute('textColor')
        this.__defineAttribute('textSize')
        this.__defineAttribute('textStyle')
        this.__defineAttribute('singleLine')
        this.__defineAttribute('maxLines')
        this.__defineAttribute('hint')
        this.__defineAttribute('hintColor')
    }
}

class EditComponent extends PureComponent {
    constructor(attributes) {
        super(attributes, 'edit')
        this.__defineAttribute('text')
        this.__defineAttribute('gravity')
        this.__defineAttribute('textColor')
        this.__defineAttribute('textSize')
        this.__defineAttribute('textStyle')
        this.__defineAttribute('singleLine')
        this.__defineAttribute('maxLines')
        this.__defineAttribute('hint')
        this.__defineAttribute('hintColor')
        this.__defineAttribute('onTextChanged')
    }
}

class ImageComponent extends PureComponent {
    constructor(attributes) {
        super(attributes, 'image')
        this.__defineAttribute('src')
        this.__defineAttribute('scaleType')
    }
}

class LinearComponent extends PureGroupComponent {
    constructor(attributes, children) {
        super(attributes, 'linear', children)
        this.__defineAttribute('orientation')
        this.__defineAttribute('gravity')
        this.__defineChildAttribute('layoutGravity')
        this.__defineChildAttribute('weight')
    }
}

class FrameComponent extends PureGroupComponent {
    constructor(attributes, children) {
        super(attributes, 'frame', children)
        this.__defineChildAttribute('layoutGravity')
    }
}

class ScrollComponent extends PureGroupComponent {
    constructor(attributes, children) {
        super(attributes, 'scroll', children)
        this.__defineAttribute('fillViewport')
    }
}

class RefreshComponent extends PureGroupComponent {
    constructor(attributes, children) {
        super(attributes, 'refresh', children)
        this.__defineAttribute('refreshEnabled')
        this.__defineAttribute('loadmoreEnabled')
        this.__defineAttribute('autoRefresh')
        this.__defineAttribute('autoLoadmore')
        this.__defineAttribute('onRefresh')
        this.__defineAttribute('onLoadmore')
    }

    finishRefresh(noMoreData) {
        this.__view.finishRefresh(noMoreData)
    }

    finishLoadmore(noMoreData) {
        this.__view.finishLoadmore(noMoreData)
    }
}

class ListComponent extends PureComponent {

    constructor(attributes, headerFooters) {
        super(attributes, 'list')

        const setItemsReactive = (items) => {
            if (items && !items.__isReactive) {
                items.__isReactive = true

                items.push = (...elements) => {
                    Array.prototype.push.call(items, ...elements)
                    this.__view.addItems(elements, items.length)
                }

                items.pop = () => {
                    if (Array.prototype.pop.call(items)) {
                        this.__view.removeItemAt(items.length)
                    }
                }

                items.splice = (start, deleteCount, ...fill) => {
                    const len = items.length

                    if (start > len) {
                        start = len
                    } else if (start < 0) {
                        start = len - start
                        if (start < 0) {
                            start = 0
                        }
                    }
                    let left = len - start
                    if (deleteCount === undefined || deleteCount > left) {
                        deleteCount = left
                    } else if (deleteCount < 0) {
                        deleteCount = 0
                    }

                    Array.prototype.splice.call(items, start, deleteCount, ...(fill || []))
                    if (deleteCount > 0) {
                        for (let i = 0; i < deleteCount; i++) {
                            this.__view.removeItemAt(start)
                        }
                    }
                    if (fill && fill.length > 0) {
                        this.__view.addItems(fill, start)
                    }
                }

                items.set = (item, index) => {
                    this.items.splice(index, 1, item)
                }
            }
        }

        let _innerItems

        Object.defineProperty(this, "items", {
            get() {
                if (!_innerItems) {
                    _innerItems = []
                    setItemsReactive(_innerItems)
                }
                return _innerItems
            },
            set(items) {
                if (_innerItems !== items) {
                    _innerItems = items
                    setItemsReactive(items)
                    this.__view.setAttribute('items', items)
                }
            },
            enumerable: false,
            configurable: false
        })
        this.__defineAttribute('renderItem')

        this.__headers = []
        this.__footers = []

        if (headerFooters) {    // HeaderFooterStruct
            for (let c of headerFooters) {
                if (c && c.component) {
                    if (c.isHeader) {
                        this.addHeader(c.component)
                    } else {
                        this.addFooter(c.component)
                    }
                }
            }
        }
    }

    addHeader(component) {
        if (this.__headers.indexOf(component) < 0) {
            this.__headers.push(component)
            this.__view.addHeader(component.__view)
            component.__onViewAttached()
        } else {
            throw new Error('组件已在header中，无法重复添加')
        }
    }

    addFooter(component) {
        if (this.__footers.indexOf(component) < 0) {
            this.__footers.push(component)
            this.__view.addFooter(component.__view)
            component.__onViewAttached()
        } else {
            throw new Error('组件已在footer中，无法重复添加')
        }
    }

    removeHeader(component) {
        let index = this.__headers.indexOf(component);
        if (index >= 0) {
            this.__headers.splice(index, 1)
            this.__view.removeHeader(component.__view)
            component.__onViewDetached()
        } else {
            throw new Error('组件不在header中，无法移出')
        }
    }

    removeFooter(component) {
        let index = this.__footers.indexOf(component);
        if (index >= 0) {
            this.__footers.splice(index, 1)
            this.__view.removeFooter(component.__view)
            component.__onViewDetached()
        } else {
            throw new Error('组件不在footer中，无法移出')
        }
    }

    onDetach() {
        for (let c of this.__headers) {
            c.__onViewDetached()
        }
        for (let c of this.__footers) {
            c.__onViewDetached()
        }
    }
}

class HeaderFooterStruct {
    constructor(component, isHeader) {
        this.component = component
        this.isHeader = isHeader
    }
}

class SlotStruct {
    constructor(component, name) {
        this.component = component
        this.name = name
    }
}

export default {
    createElement(type, props, ...children) {

        let filteredChildren = []
        for (let c of children) {
            if (c) {
                if (c instanceof Array) {
                    for (let c1 of c) {
                        filteredChildren.push(c1)
                    }
                } else {
                    filteredChildren.push(c)
                }
            }
        }

        let comp
        if (type instanceof Function) {
            comp = new type(props)
            for (let slot of filteredChildren) {    // filteredChildren are slots
                comp.onAddSlot(slot.component, slot.name)
            }
        } else if (type === 'view') {
            comp = new ViewComponent(props)
        } else if (type === 'text') {
            comp = new TextComponent(props)
        } else if (type === 'edit') {
            comp = new EditComponent(props)
        } else if (type === 'image') {
            comp = new ImageComponent(props)
        } else if (type === 'frame') {
            comp = new FrameComponent(props, filteredChildren)
        } else if (type === 'linear') {
            comp = new LinearComponent(props, filteredChildren)
        } else if (type === 'scroll') {
            comp = new ScrollComponent(props, filteredChildren)
        } else if (type === 'refresh') {
            comp = new RefreshComponent(props, filteredChildren)
        } else if (type === 'list') {
            comp = new ListComponent(props, filteredChildren)
        } else if (type === 'header' || type === 'footer') {
            if (filteredChildren.length > 1) {
                throw new Error(type + '标签只接受一个子视图/组件')
            }
            if (filteredChildren.length === 1) {
                comp = new HeaderFooterStruct(filteredChildren[0], type === 'header')
            } else {
                comp = null
            }
        } else if (type === 'slot') {
            if (filteredChildren.length > 1) {
                throw new Error('slot标签只接受一个子视图/组件')
            }
            if (filteredChildren.length === 1) {
                if (!props.name) {
                    throw new Error('slot标签必须要有name属性')
                }
                comp = new SlotStruct(filteredChildren[0], props.name)
            } else {
                comp = null
            }
        } else {
            throw new Error('未知标签:' + type)
        }
        return comp
    },
    startPage(PageClass) {
        let pageObj
        page.on('ready', () => {
            pageObj = new PageClass(page.param || {})
            if (pageObj) {
                page.view.addChild(pageObj.__root.__view)
                pageObj.__onViewAttached()
                pageObj.pageReady()
            }
        })
        page.on('show', () => {
            if (pageObj) {
                pageObj.pageShow();
            }
        })
        page.on('hide', () => {
            if (pageObj) {
                pageObj.pageHide();
            }
        })
        page.on('close', () => {
            if (pageObj) {
                pageObj.pageClose()
                pageObj.__onViewDetached()
            }
        })
    }
}

export {DefinedComponent}
