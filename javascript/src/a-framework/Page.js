export default class Page {
    constructor(props) {
        this.props = props || {}
        this.__root = this.render()
    }

    render() {
        return null
    }

    pageReady() {

    }

    pageShow() {

    }

    pageHide() {

    }

    pageClose() {

    }

    __onViewAttached() {
        this.__root.__onViewAttached()
    }

    __onViewDetached() {
        this.__root.__onViewDetached()
    }

    $(id) {
        return this.findViewById(id)
    }

    findViewById(id) {
        if (this.__root) {
            return this.__root.findViewById(id)
        }
        return null
    }

    back(result) {
        page.back(result || {})
    }

    navigate(url, param) {
        page.navigate(url, param || {})
    }
}
