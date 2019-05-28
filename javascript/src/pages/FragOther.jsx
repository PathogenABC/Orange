import React, {DefinedComponent} from '../a-framework/React'

export default class FragOther extends DefinedComponent {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <text text="other"/>
        )
    }

    onShow() {
        localStorage.of('user').setItem('myKey', undefined)

        setTimeout((title, name) => {
            app.toast(JSON.stringify({
                title, name
            }))
        }, 2000, '章三', '李四')

        let i = 0
        this.interval = setInterval(() => {
            app.toast('interval - ' + (i++))
            // clearInterval(this.interval)
        }, 3000)
    }

    onHide() {
        clearInterval(this.interval)
    }
}

const styles = {}
