import React, {DefinedComponent} from '../a-framework/React'

export default class TabBar extends DefinedComponent {
    constructor(props) {
        super(props)
        this.currIndex = -1

        this.tabEls = []
        for (let tab of this.props.tabs) {
            this.tabEls.push(this.$(tab[1]))
        }
    }

    onMount() {
        this.onTabClick(0)
    }

    render() {
        return (
            <linear style={styles.root}>
                {this.renderTabs()}
            </linear>
        )
    }

    renderTabs() {
        const arr = []
        let i = 0
        for (let tab of this.props.tabs) {
            arr.push(
                <linear style={styles.tabItem} onClick={this.onTabClick.bind(this, i)}>
                    <text id={tab[1]} text={tab[1]} style={styles.itemText}/>
                </linear>
            )
            i++
        }
        return arr
    }

    onTabClick(index) {
        if (this.currIndex !== index) {
            console.log('System.err', JSON.stringify(this.props))
            if (this.props.onTabChanged) {
                this.props.onTabChanged(index, this.currIndex)
            }
            if (this.currIndex >= 0) {
                this.tabEls[this.currIndex].textColor = 'black'
            }
            this.tabEls[index].textColor = 'red'
            this.currIndex = index
        }
    }
}

const styles = {
    root: {
        layoutWidth: 'match',
        layoutHeight: 50,
        orientation: 'horizontal',
        backgroundColor: 'white'
    },
    tabItem: {
        weight: 1,
        layoutHeight: 'match',
        orientation: 'vertical',
        gravity: 'center'
    },
    itemText: {
        textColor: 'black',
        textSize: 15
    }
}
