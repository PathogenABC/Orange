import React, {DefinedComponent} from '../a-framework/React'
import StatusBar from "../component/StatusBar.jsx"

class Item extends DefinedComponent {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <linear style={{layoutWidth: 'match', layoutHeight: 'wrap', orientation: 'vertical'}}>
                <linear style={styles.item}>
                    <image style={styles.itemPic}
                           src='https://upload.jianshu.io/users/upload_avatars/3678546/6cbe4b23-c87d-4662-82f4-47d762aef233.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240'/>
                    <text text={this.props.data.title} style={styles.itemText}/>
                </linear>
                <view style={{layoutWidth: 'match', layoutHeight: 0.5, backgroundColor: '#e1e1e1'}}/>
            </linear>
        )
    }

    onMount() {
        console.log('Demo', "onMount: " + this.props.index)
    }

    onUnmount() {
        console.log('Demo', "onUnmount: " + this.props.index)
    }
}

export default class FragKnow extends DefinedComponent {

    constructor(props) {
        super(props)
        this.list = this.$('list');
        this.refresh = this.$('refresh')
    }

    render() {
        return (
            <linear style={styles.root}>
                <StatusBar left='返回' title='知识体系'/>
                <refresh id='refresh' style={styles.refresh} onRefresh={() => this.refreshData()} onLoadmore={() => this.loadmoreData()}>
                    <list id="list" style={styles.list} renderItem={this.renderItem.bind(this)}/>
                </refresh>
            </linear>
        )
    }

    renderItem(item, index) {
        return <Item data={item} index={index}/>
    }

    onShow() {
        localStorage.of('user').setItem('myKey', '知识体系');
    }

    getData(page) {
        return new Promise((resolve, reject) => {
            http.get(`https://www.wanandroid.com/article/list/${page}/json`, (result, error) => {
                if (error) {
                    reject(error)
                } else {
                    resolve(result.data.datas || [])
                }
            })
        }).catch(e => {
            app.toast(e)
        })
    }

    refreshData() {
        this.getData(0).then(items => {
            this.list.items.splice(0, this.list.items.length, ...items)
            this.refresh.finishRefresh(items.length < 20)
            console.log('Exception', 'items.length = ' + this.list.items.length)
        }).catch(e => {
            console.log('Exception', e)
        })
    }

    loadmoreData() {
        this.getData(Math.floor(this.list.items.length / 20)).then(items => {
            this.list.items.push(...items)
            this.refresh.finishLoadmore(items.length < 20)
        }).catch(e => {
            console.log('Exception', e)
        })
    }
}

const styles = {
    root: {
        layoutWidth: 'match',
        layoutHeight: 'match',
        orientation: 'vertical'
    },
    refresh: {
        layoutWidth: 'match',
        layoutHeight: 'match',
        autoRefresh: true
    },
    list: {
        layoutWidth: 'match',
        layoutHeight: 'match'
    },
    item: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        gravity: 'centerVertical',
        padding: 10
    },
    itemPic: {
        layoutWidth: 60,
        layoutHeight: 60,
        borderRadius: 10
    },
    itemText: {
        layoutWidth: 'wrap',
        layoutHeight: 'wrap',
        paddingLeft: 10,
        textSize: 16,
        textColor: '#444444'
    },
    header: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        textSize: 18,
        textColor: 'red',
        backgroundColor: 'green'
    },
    image: {
        layoutWidth: 200,
        layoutHeight: 200,
        borderRadius: 100,
        borderColor: 'red',
        borderWidth: 20,
        padding: 30
    }
}
