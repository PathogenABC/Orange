import React, {DefinedComponent} from '../a-framework/React'

export default class FragHome extends DefinedComponent {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <scroll style={styles.scroll}>
                <linear id="content" style={styles.content}/>
            </scroll>
        )
    }

    onMount() {
        new Promise((resolve, reject) => {
            http.get('https://www.wanandroid.com/article/top/json', (result, error) => {
                if (error) {
                    reject(error)
                } else {
                    resolve(result.data || [])
                }
            })
        }).then(tops => {
            for (let item of tops) {
                item.isTop = true
            }
            return new Promise((resolve, reject) => {
                http.get('https://www.wanandroid.com/article/list/0/json', (result, error) => {
                    if (error) {
                        reject(error)
                    } else {
                        resolve(result.data ? (result.data.datas || []) : [])
                    }
                })
            }).then(items => {
                for (let item of items) {
                    tops.push(item)
                }
                return tops
            })
        }).then(items => {
            let $content = this.$('content');
            for (let item of items) {
                $content.addChild(
                    <text text={item.title} style={styles.item}/>
                )
            }
        })
    }

    onShow() {
        let item = localStorage.of('user').getItem('myKey')
        app.toast(JSON.stringify(item || 'undefined'))
    }
}

const styles = {
    scroll: {
        layoutWidth: 'match',
        layoutHeight: 'match',
        backgroundColor: 'white',
        fillViewport: true,
        paddingBottom: 30,
        clipToPadding: false
    },
    content: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        orientation: 'vertical'
    },
    itemText: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        padding: 15,
        textSize: 18,
        textStyle: 'underline',
        textColor: 'black',
        borderRadius: 20,
        borderWidth: 2,
        borderColor: 'blue',
        margin: 10
    }
}
