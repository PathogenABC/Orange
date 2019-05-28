import React from '../a-framework/React'
import Page from '../a-framework/Page'
import TabBar from "../component/TabBar.jsx";
import FragHome from "./FragHome.jsx";
import FragKnow from "./FragKnow.jsx";
import FragNavi from "./FragNavi.jsx";
import FragOther from "./FragOther.jsx";

let user = modules.require('user')

class MainPage extends Page {

    constructor(props) {
        super(props)
        this.fragments = [this.$('frag0'), this.$('frag1'), this.$('frag2'), this.$('frag3')]
        for (let f of this.fragments) {
            f.visibility = 'gone'
        }
    }

    render() {

        const tabs = [['xxx', '首页'], ['xxx', '知识体系'], ['xxx', '导航'], ['xxx', '其他']]

        return (
            <linear style={styles.root}>
                <frame style={styles.content}>
                    <FragHome id='frag0'/>
                    <FragKnow id='frag1'/>
                    <FragNavi id='frag2'/>
                    <FragOther id='frag3'/>
                </frame>

                <TabBar tabs={tabs} onTabChanged={this.onTabChanged.bind(this)}/>
            </linear>
        )
    }

    onTabChanged(index, preIndex) {

        let arr = []
        for (let key in this) {
            arr.push(key)
        }

        console.log('System.err', JSON.stringify(arr))


        if (preIndex >= 0) {
            this.fragments[preIndex].visibility = 'gone'
            if (this.fragments[preIndex].onHide) {
                this.fragments[preIndex].onHide()
            }
        }
        this.fragments[index].visibility = 'visible'
        if (this.fragments[index].onShow) {
            this.fragments[index].onShow()
        }
    }

    pageReady() {
        app.toast(user.height + user.getName())
    }
}

const styles = {
    root: {
        layoutWidth: 'match',
        layoutHeight: 'match',
        orientation: 'vertical'
    },
    content: {
        layoutWidth: 'match',
        weight: 1
    }
}

React.startPage(MainPage)
