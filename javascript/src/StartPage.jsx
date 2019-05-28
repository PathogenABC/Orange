import Page from "./a-framework/Page";
import React from "./a-framework/React";
import StatusBar from "./component/StatusBar.jsx";

class StartPage extends Page {
    constructor(props) {
        super(props)

        this.text = '我爱你'
    }

    render() {
        return (
            <linear style={styles.root} onClick={() => this.onClick()}>

                <StatusBar id='title' left='返回' title='Start'/>

                <view id='view' style={styles.view} onClick={() => this.goNext()}/>
            </linear>
        )
    }

    onClick() {
        let view = this.$('view')
        view.layoutWidth = view.width + 10
    }

    goNext() {
        this.navigate('pages/MainPage', {})
        // app.toast(this.text)
    }
}

const styles = {
    root: {
        backgroundColor: 'blue',
        layoutWidth: 'match',
        layoutHeight: 300,
        orientation: 'vertical'
    },
    view: {
        backgroundColor: 'red',
        layoutWidth: 100,
        layoutHeight: 100,
        layoutGravity: 'center'
    }
}

React.startPage(StartPage)
