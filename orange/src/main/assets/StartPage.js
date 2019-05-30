/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/StartPage.jsx");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/StartPage.jsx":
/*!***************************!*\
  !*** ./src/StartPage.jsx ***!
  \***************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _a_framework_Page__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./a-framework/Page */ \"./src/a-framework/Page.js\");\n/* harmony import */ var _a_framework_React__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./a-framework/React */ \"./src/a-framework/React.js\");\n/* harmony import */ var _component_StatusBar_jsx__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./component/StatusBar.jsx */ \"./src/component/StatusBar.jsx\");\n\n\n\n\nclass StartPage extends _a_framework_Page__WEBPACK_IMPORTED_MODULE_0__[\"default\"] {\n  constructor(props) {\n    super(props);\n    this.text = '我爱你';\n  }\n\n  render() {\n    return _a_framework_React__WEBPACK_IMPORTED_MODULE_1__[\"default\"].createElement(\"linear\", {\n      style: styles.root,\n      onClick: () => this.onClick()\n    }, _a_framework_React__WEBPACK_IMPORTED_MODULE_1__[\"default\"].createElement(_component_StatusBar_jsx__WEBPACK_IMPORTED_MODULE_2__[\"default\"], {\n      id: \"title\",\n      left: \"\\u8FD4\\u56DE\",\n      title: \"Start\"\n    }), _a_framework_React__WEBPACK_IMPORTED_MODULE_1__[\"default\"].createElement(\"view\", {\n      id: \"view\",\n      style: styles.view,\n      onClick: () => this.goNext()\n    }));\n  }\n\n  onClick() {\n    let view = this.$('view');\n    view.layoutWidth = view.width + 10;\n  }\n\n  goNext() {\n    this.navigate('pages/MainPage', {}); // app.toast(this.text)\n  }\n\n}\n\nconst styles = {\n  root: {\n    backgroundColor: 'blue',\n    layoutWidth: 'match',\n    layoutHeight: 300,\n    orientation: 'vertical'\n  },\n  view: {\n    backgroundColor: 'red',\n    layoutWidth: 100,\n    layoutHeight: 100,\n    layoutGravity: 'center'\n  }\n};\n_a_framework_React__WEBPACK_IMPORTED_MODULE_1__[\"default\"].startPage(StartPage);\n\n//# sourceURL=webpack:///./src/StartPage.jsx?");

/***/ }),

/***/ "./src/a-framework/Page.js":
/*!*********************************!*\
  !*** ./src/a-framework/Page.js ***!
  \*********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"default\", function() { return Page; });\nclass Page {\n    constructor(props) {\n        this.props = props || {}\n        this.__root = this.render()\n    }\n\n    render() {\n        return null\n    }\n\n    pageReady() {\n\n    }\n\n    pageShow() {\n\n    }\n\n    pageHide() {\n\n    }\n\n    pageClose() {\n\n    }\n\n    __onViewAttached() {\n        this.__root.__onViewAttached()\n    }\n\n    __onViewDetached() {\n        this.__root.__onViewDetached()\n    }\n\n    $(id) {\n        return this.findViewById(id)\n    }\n\n    findViewById(id) {\n        if (this.__root) {\n            return this.__root.findViewById(id)\n        }\n        return null\n    }\n\n    back(result) {\n        page.back(result || {})\n    }\n\n    navigate(url, param) {\n        page.navigate(url, param || {})\n    }\n}\n\n\n//# sourceURL=webpack:///./src/a-framework/Page.js?");

/***/ }),

/***/ "./src/a-framework/React.js":
/*!**********************************!*\
  !*** ./src/a-framework/React.js ***!
  \**********************************/
/*! exports provided: default, DefinedComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"DefinedComponent\", function() { return DefinedComponent; });\n/**\n * 组件\n */\nclass Component {\n    constructor() {\n        this.__id = null\n        this.__view = null\n        this.__parent = null\n        this.__viewAttached = null\n    }\n\n    __onViewAttached() {\n    }\n\n    __onViewDetached() {\n    }\n\n    $(id) {\n        return this.findViewById(id)\n    }\n\n    findViewById(id) {\n        return null\n    }\n}\n\nclass PureComponent extends Component {\n    constructor(attributes, type) {\n        super()\n        attributes = attributes || {}\n        this.__id = attributes.id\n        delete attributes.id\n\n        this.__parent = null\n        this.__children = []\n        this.__bindComponent = null\n        this.__view = ui.createElement(type)\n        this.__setAttributesToView(attributes)\n\n        // 可视属性要特殊处理\n        this.__defineAttribute('visibility', (_, visibility) => {\n            if (visibility !== 'none' && this.__parent && this.__parent.__viewAttached) {\n                this.__onViewAttached()\n            }\n        })\n\n        this.__defineProperty(\"width\", \"getWidth\")\n        this.__defineProperty(\"height\", \"getHeight\")\n\n        this.__defineAttribute('layoutWidth')\n        this.__defineAttribute('layoutHeight')\n\n        this.__defineAttribute('paddingLeft')\n        this.__defineAttribute('paddingTop')\n        this.__defineAttribute('paddingRight')\n        this.__defineAttribute('paddingBottom')\n\n        this.__defineAttribute('marginLeft')\n        this.__defineAttribute('marginTop')\n        this.__defineAttribute('marginRight')\n        this.__defineAttribute('marginBottom')\n\n        this.__defineAttribute('backgroundColor')\n\n        this.__defineAttribute('borderWidth')\n        this.__defineAttribute('borderColor')\n        this.__defineAttribute('borderRadius')\n\n        this.__defineAttribute('onSizeChanged')\n        this.__defineAttribute('onClick')\n        this.__defineAttribute('onLongClick')\n    }\n\n    __defineProperty(propName, getFuncName) {\n        Object.defineProperty(this, propName, {\n            get() {\n                return this.__view[getFuncName]()\n            },\n            enumerable: false,\n            configurable: false\n        })\n    }\n\n    __defineAttribute(attrName, afterSet = null) {\n        Object.defineProperty(this, attrName, {\n            get() {\n                return this.__view.getAttribute(attrName)\n            },\n            set(v) {\n                if (this.__view.getAttribute(attrName) !== v) {\n                    this.__view.setAttribute(attrName, v)\n                    if (afterSet) {\n                        afterSet(attrName, v)\n                    }\n                }\n            },\n            enumerable: false,\n            configurable: false\n        })\n    }\n\n    __setAttributesToView(attributes) {\n        if (this.__view) {\n            if (attributes.hasOwnProperty('style')) {\n                const styles = attributes['style']\n                delete attributes['style']\n                attributes = Object.assign({}, styles, attributes)\n            }\n            for (let key in attributes) {\n                if (attributes.hasOwnProperty(key)) {\n                    this.__view.setAttribute(key, attributes[key])\n                }\n            }\n        }\n    }\n\n    __onViewAttached() {\n        const visibility = this.visibility === undefined ? 'visible' : this.visibility\n        if (visibility !== 'gone' && !this.__viewAttached) {\n            this.__viewAttached = true\n            for (let child of this.__children) {\n                child.__onViewAttached()\n            }\n            this.onAttach()\n\n            if (this.__bindComponent) {\n                this.__bindComponent.onMount()\n            }\n        }\n    }\n\n    __onViewDetached() {\n        if (this.__viewAttached) {\n            this.__viewAttached = false\n            for (let child of this.__children) {\n                child.__onViewDetached()\n            }\n            this.onDetach()\n\n            if (this.__bindComponent) {\n                this.__bindComponent.onUnmount()\n            }\n        }\n    }\n\n    onAttach() {\n\n    }\n\n    onDetach() {\n        // impl\n    }\n\n    findViewById(id) {\n        return this.__id === id ? this : null\n    }\n}\n\nclass PureGroupComponent extends PureComponent {\n    constructor(attributes, type, children) {\n        super(attributes, type)\n        this.__childAttributePrototype = {}\n        for (let c of children || []) {\n            this.addChild(c)\n        }\n    }\n\n    __defineChildAttribute(attrName) {\n        Object.defineProperty(this.__childAttributePrototype, attrName, {\n            get() {\n                return this.__view.getAttribute(attrName)\n            },\n            set(v) {\n                if (this.__view.getAttribute(attrName) !== v) {\n                    this.__view.setAttribute(attrName, v)\n                }\n            },\n            enumerable: false,\n            configurable: false\n        })\n    }\n\n    findViewById(id) {\n        const v = super.findViewById(id)\n        if (v) {\n            return v\n        }\n        for (let c of this.__children) {\n            if (c instanceof DefinedComponent) {\n                if (c.__id === id) {\n                    return c\n                }\n            } else {\n                const v = c.findViewById(id)\n                if (v) {\n                    return v\n                }\n            }\n        }\n        return null\n    }\n\n    addChild(child, index) {\n        if ((index !== 0 && !index) || index <= -1) {\n            index = this.__children.length\n        }\n        if (child instanceof DefinedComponent) {\n            if (!child.__parent) {\n                this.__children.splice(index, 0, child)\n                child.__parent = this\n                child.__root.prototype = this.__childAttributePrototype\n                this.__view.addChild(child.__root.__view, index)\n                if (this.__viewAttached) {\n                    child.__onViewAttached()\n                }\n            }\n        } else {\n            if (!child.__parent) {\n                this.__children.splice(index, 0, child)\n                child.__parent = this\n                child.prototype = this.__childAttributePrototype\n                this.__view.addChild(child.__view, index)\n                if (this.__viewAttached) {\n                    child.__onViewAttached()\n                }\n            }\n        }\n    }\n\n    removeChild(child) {\n        if (child instanceof DefinedComponent) {\n            if (child.__parent === this) {\n                const index = this.__children.indexOf(child)\n                this.__children.splice(index, 1)\n                this.__view.removeChild(child.__root.__view)\n                child.__parent = null\n                child.__root.prototype = null\n                if (this.__viewAttached) {\n                    child.__onViewDetached()\n                }\n            }\n        } else {\n            if (child.__parent === this) {\n                const index = this.__children.indexOf(child)\n                this.__children.splice(index, 1)\n                this.__view.removeChild(child.__view)\n                child.__parent = null\n                child.prototype = null\n                if (this.__viewAttached) {\n                    child.__onViewDetached()\n                }\n            }\n        }\n    }\n\n    removeAllViews() {\n        const copy = []\n        copy.push(this.__children)\n        for (let c of copy) {\n            this.removeChild(c)\n        }\n    }\n}\n\nclass DefinedComponent extends Component {\n    constructor(props) {\n        super()\n        this.props = props || {}\n        this.__id = this.props.id\n        this.__parent = null\n\n        this.__root = this.render()\n        if (this.__root instanceof DefinedComponent) {\n            throw new Error(\"自定义组件的根视图不能是自定义组件\")\n        }\n        this.__root.__parent = this\n        this.__root.__bindComponent = this\n        this.__view = this.__root.__view\n\n        // 定义visibility属性\n        Object.defineProperty(this, 'visibility', {\n            get() {\n                return this.__root.visibility\n            },\n            set(v) {\n                this.__root.visibility = v\n            },\n            enumerable: false,\n            configurable: false\n        })\n    }\n\n    onAddSlot(slotComponent, slotName) {\n        // impl\n    }\n\n    onMount() {\n        // impl\n    }\n\n    onUnmount() {\n        // impl\n    }\n\n    findViewById(id) {\n        return this.__root.findViewById(id);\n    }\n\n    __onViewAttached() {\n        this.__viewAttached = true\n        this.__root.__onViewAttached();\n    }\n\n    __onViewDetached() {\n        this.__viewAttached = false\n        this.__root.__onViewDetached()\n    }\n}\n\nclass ViewComponent extends PureComponent {\n    constructor(attributes) {\n        super(attributes, 'view')\n    }\n}\n\nclass TextComponent extends PureComponent {\n    constructor(attributes) {\n        super(attributes, 'text')\n        this.__defineAttribute('text')\n        this.__defineAttribute('gravity')\n        this.__defineAttribute('textColor')\n        this.__defineAttribute('textSize')\n        this.__defineAttribute('textStyle')\n        this.__defineAttribute('singleLine')\n        this.__defineAttribute('maxLines')\n        this.__defineAttribute('hint')\n        this.__defineAttribute('hintColor')\n    }\n}\n\nclass EditComponent extends PureComponent {\n    constructor(attributes) {\n        super(attributes, 'edit')\n        this.__defineAttribute('text')\n        this.__defineAttribute('gravity')\n        this.__defineAttribute('textColor')\n        this.__defineAttribute('textSize')\n        this.__defineAttribute('textStyle')\n        this.__defineAttribute('singleLine')\n        this.__defineAttribute('maxLines')\n        this.__defineAttribute('hint')\n        this.__defineAttribute('hintColor')\n        this.__defineAttribute('onTextChanged')\n    }\n}\n\nclass ImageComponent extends PureComponent {\n    constructor(attributes) {\n        super(attributes, 'image')\n        this.__defineAttribute('src')\n        this.__defineAttribute('scaleType')\n    }\n}\n\nclass LinearComponent extends PureGroupComponent {\n    constructor(attributes, children) {\n        super(attributes, 'linear', children)\n        this.__defineAttribute('orientation')\n        this.__defineAttribute('gravity')\n        this.__defineChildAttribute('layoutGravity')\n        this.__defineChildAttribute('weight')\n    }\n}\n\nclass FrameComponent extends PureGroupComponent {\n    constructor(attributes, children) {\n        super(attributes, 'frame', children)\n        this.__defineChildAttribute('layoutGravity')\n    }\n}\n\nclass ScrollComponent extends PureGroupComponent {\n    constructor(attributes, children) {\n        super(attributes, 'scroll', children)\n        this.__defineAttribute('fillViewport')\n    }\n}\n\nclass RefreshComponent extends PureGroupComponent {\n    constructor(attributes, children) {\n        super(attributes, 'refresh', children)\n        this.__defineAttribute('refreshEnabled')\n        this.__defineAttribute('loadmoreEnabled')\n        this.__defineAttribute('autoRefresh')\n        this.__defineAttribute('autoLoadmore')\n        this.__defineAttribute('onRefresh')\n        this.__defineAttribute('onLoadmore')\n    }\n\n    finishRefresh(noMoreData) {\n        this.__view.finishRefresh(noMoreData)\n    }\n\n    finishLoadmore(noMoreData) {\n        this.__view.finishLoadmore(noMoreData)\n    }\n}\n\nclass ListComponent extends PureComponent {\n\n    constructor(attributes, headerFooters) {\n        super(attributes, 'list')\n\n        const setItemsReactive = (items) => {\n            if (items && !items.__isReactive) {\n                items.__isReactive = true\n\n                items.push = (...elements) => {\n                    Array.prototype.push.call(items, ...elements)\n                    this.__view.addItems(elements, items.length)\n                }\n\n                items.pop = () => {\n                    if (Array.prototype.pop.call(items)) {\n                        this.__view.removeItemAt(items.length)\n                    }\n                }\n\n                items.splice = (start, deleteCount, ...fill) => {\n                    const len = items.length\n\n                    if (start > len) {\n                        start = len\n                    } else if (start < 0) {\n                        start = len - start\n                        if (start < 0) {\n                            start = 0\n                        }\n                    }\n                    let left = len - start\n                    if (deleteCount === undefined || deleteCount > left) {\n                        deleteCount = left\n                    } else if (deleteCount < 0) {\n                        deleteCount = 0\n                    }\n\n                    Array.prototype.splice.call(items, start, deleteCount, ...(fill || []))\n                    if (deleteCount > 0) {\n                        for (let i = 0; i < deleteCount; i++) {\n                            this.__view.removeItemAt(start)\n                        }\n                    }\n                    if (fill && fill.length > 0) {\n                        this.__view.addItems(fill, start)\n                    }\n                }\n\n                items.set = (item, index) => {\n                    this.items.splice(index, 1, item)\n                }\n            }\n        }\n\n        let _innerItems\n\n        Object.defineProperty(this, \"items\", {\n            get() {\n                if (!_innerItems) {\n                    _innerItems = []\n                    setItemsReactive(_innerItems)\n                }\n                return _innerItems\n            },\n            set(items) {\n                if (_innerItems !== items) {\n                    _innerItems = items\n                    setItemsReactive(items)\n                    this.__view.setAttribute('items', items)\n                }\n            },\n            enumerable: false,\n            configurable: false\n        })\n        this.__defineAttribute('renderItem')\n\n        this.__headers = []\n        this.__footers = []\n\n        if (headerFooters) {    // HeaderFooterStruct\n            for (let c of headerFooters) {\n                if (c && c.component) {\n                    if (c.isHeader) {\n                        this.addHeader(c.component)\n                    } else {\n                        this.addFooter(c.component)\n                    }\n                }\n            }\n        }\n    }\n\n    addHeader(component) {\n        if (this.__headers.indexOf(component) < 0) {\n            this.__headers.push(component)\n            this.__view.addHeader(component.__view)\n            component.__onViewAttached()\n        } else {\n            throw new Error('组件已在header中，无法重复添加')\n        }\n    }\n\n    addFooter(component) {\n        if (this.__footers.indexOf(component) < 0) {\n            this.__footers.push(component)\n            this.__view.addFooter(component.__view)\n            component.__onViewAttached()\n        } else {\n            throw new Error('组件已在footer中，无法重复添加')\n        }\n    }\n\n    removeHeader(component) {\n        let index = this.__headers.indexOf(component);\n        if (index >= 0) {\n            this.__headers.splice(index, 1)\n            this.__view.removeHeader(component.__view)\n            component.__onViewDetached()\n        } else {\n            throw new Error('组件不在header中，无法移出')\n        }\n    }\n\n    removeFooter(component) {\n        let index = this.__footers.indexOf(component);\n        if (index >= 0) {\n            this.__footers.splice(index, 1)\n            this.__view.removeFooter(component.__view)\n            component.__onViewDetached()\n        } else {\n            throw new Error('组件不在footer中，无法移出')\n        }\n    }\n\n    onDetach() {\n        for (let c of this.__headers) {\n            c.__onViewDetached()\n        }\n        for (let c of this.__footers) {\n            c.__onViewDetached()\n        }\n    }\n}\n\nclass HeaderFooterStruct {\n    constructor(component, isHeader) {\n        this.component = component\n        this.isHeader = isHeader\n    }\n}\n\nclass SlotStruct {\n    constructor(component, name) {\n        this.component = component\n        this.name = name\n    }\n}\n\n/* harmony default export */ __webpack_exports__[\"default\"] = ({\n    createElement(type, props, ...children) {\n\n        let filteredChildren = []\n        for (let c of children) {\n            if (c) {\n                if (c instanceof Array) {\n                    for (let c1 of c) {\n                        filteredChildren.push(c1)\n                    }\n                } else {\n                    filteredChildren.push(c)\n                }\n            }\n        }\n\n        let comp\n        if (type instanceof Function) {\n            comp = new type(props)\n            for (let slot of filteredChildren) {    // filteredChildren are slots\n                comp.onAddSlot(slot.component, slot.name)\n            }\n        } else if (type === 'view') {\n            comp = new ViewComponent(props)\n        } else if (type === 'text') {\n            comp = new TextComponent(props)\n        } else if (type === 'edit') {\n            comp = new EditComponent(props)\n        } else if (type === 'image') {\n            comp = new ImageComponent(props)\n        } else if (type === 'frame') {\n            comp = new FrameComponent(props, filteredChildren)\n        } else if (type === 'linear') {\n            comp = new LinearComponent(props, filteredChildren)\n        } else if (type === 'scroll') {\n            comp = new ScrollComponent(props, filteredChildren)\n        } else if (type === 'refresh') {\n            comp = new RefreshComponent(props, filteredChildren)\n        } else if (type === 'list') {\n            comp = new ListComponent(props, filteredChildren)\n        } else if (type === 'header' || type === 'footer') {\n            if (filteredChildren.length > 1) {\n                throw new Error(type + '标签只接受一个子视图/组件')\n            }\n            if (filteredChildren.length === 1) {\n                comp = new HeaderFooterStruct(filteredChildren[0], type === 'header')\n            } else {\n                comp = null\n            }\n        } else if (type === 'slot') {\n            if (filteredChildren.length > 1) {\n                throw new Error('slot标签只接受一个子视图/组件')\n            }\n            if (filteredChildren.length === 1) {\n                if (!props.name) {\n                    throw new Error('slot标签必须要有name属性')\n                }\n                comp = new SlotStruct(filteredChildren[0], props.name)\n            } else {\n                comp = null\n            }\n        } else {\n            throw new Error('未知标签:' + type)\n        }\n        return comp\n    },\n    startPage(PageClass) {\n        let pageObj\n        page.on('ready', () => {\n            pageObj = new PageClass(page.param || {})\n            if (pageObj) {\n                page.view.addChild(pageObj.__root.__view)\n                pageObj.__onViewAttached()\n                pageObj.pageReady()\n            }\n        })\n        page.on('show', () => {\n            if (pageObj) {\n                pageObj.pageShow();\n            }\n        })\n        page.on('hide', () => {\n            if (pageObj) {\n                pageObj.pageHide();\n            }\n        })\n        page.on('close', () => {\n            if (pageObj) {\n                pageObj.pageClose()\n                pageObj.__onViewDetached()\n                pageObj = null\n            }\n        })\n    }\n});\n\n\n\n\n//# sourceURL=webpack:///./src/a-framework/React.js?");

/***/ }),

/***/ "./src/component/StatusBar.jsx":
/*!*************************************!*\
  !*** ./src/component/StatusBar.jsx ***!
  \*************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"default\", function() { return StatusBar; });\n/* harmony import */ var _a_framework_React__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../a-framework/React */ \"./src/a-framework/React.js\");\n\nclass StatusBar extends _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"DefinedComponent\"] {\n  constructor(props) {\n    super(props);\n  }\n\n  render() {\n    return _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"linear\", {\n      style: styles.root\n    }, _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"linear\", {\n      style: styles.content\n    }, _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"text\", {\n      id: \"back\",\n      style: styles.left,\n      visibility: this.props.left ? 'visible' : 'invisible',\n      text: this.props.left || '',\n      onClick: () => this.onBackClick()\n    }), _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"text\", {\n      id: \"title\",\n      style: styles.title,\n      text: this.props.title\n    }), _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"text\", {\n      style: styles.right,\n      text: this.props.right || '',\n      visibility: this.props.right ? 'visible' : 'invisible'\n    })), _a_framework_React__WEBPACK_IMPORTED_MODULE_0__[\"default\"].createElement(\"view\", {\n      style: {\n        layoutWidth: 'match',\n        layoutHeight: 0.5,\n        backgroundColor: '#e1e1e1'\n      }\n    }));\n  }\n\n  onBackClick() {\n    page.back();\n  }\n\n  onMount() {\n    app.toast('onMount: outerId = ' + this.__outerId);\n  }\n\n  onUnmount() {\n    app.toast('onUnmount');\n  }\n\n}\nconst styles = {\n  root: {\n    layoutWidth: 'match',\n    layoutHeight: 'wrap',\n    orientation: 'vertical'\n  },\n  content: {\n    layoutWidth: 'match',\n    layoutHeight: 50,\n    orientation: 'horizontal'\n  },\n  left: {\n    layoutWidth: 60,\n    layoutHeight: 'match',\n    textSize: 15,\n    textColor: '#888888',\n    gravity: 'center'\n  },\n  right: {\n    layoutWidth: 60,\n    layoutHeight: 'match',\n    textSize: 15,\n    textColor: '#888888',\n    gravity: 'center'\n  },\n  title: {\n    weight: 1,\n    layoutHeight: 'match',\n    textColor: 'black',\n    textSize: 18,\n    textStyle: 'bold',\n    gravity: 'center'\n  }\n};\n\n//# sourceURL=webpack:///./src/component/StatusBar.jsx?");

/***/ })

/******/ });