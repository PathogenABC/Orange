import React, {DefinedComponent} from '../a-framework/React'

export default class StatusBar extends DefinedComponent {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <linear style={styles.root}>
                <linear style={styles.content}>
                    <text id='back' style={styles.left} visibility={this.props.left ? 'visible' : 'invisible'} text={this.props.left || ''} onClick={() => this.onBackClick()}/>
                    <text id='title' style={styles.title} text={this.props.title}/>
                    <text style={styles.right} text={this.props.right || ''} visibility={this.props.right ? 'visible' : 'invisible'}/>
                </linear>
                <view style={{layoutWidth: 'match', layoutHeight: 0.5, backgroundColor: '#e1e1e1'}}/>
            </linear>
        )
    }

    onBackClick() {
        page.back()
    }

    onMount() {
        app.toast('onMount: outerId = ' + this.__outerId)
    }

    onUnmount() {
        app.toast('onUnmount')
    }
}

const styles = {
    root: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        orientation: 'vertical'
    },
    content: {
        layoutWidth: 'match',
        layoutHeight: 50,
        orientation: 'horizontal'
    },
    left: {
        layoutWidth: 60,
        layoutHeight: 'match',
        textSize: 15,
        textColor: '#888888',
        gravity: 'center'
    },
    right: {
        layoutWidth: 60,
        layoutHeight: 'match',
        textSize: 15,
        textColor: '#888888',
        gravity: 'center'
    },
    title: {
        weight: 1,
        layoutHeight: 'match',
        textColor: 'black',
        textSize: 18,
        textStyle: 'bold',
        gravity: 'center'
    }
}
