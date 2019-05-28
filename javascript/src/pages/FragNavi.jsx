import React, {DefinedComponent} from '../a-framework/React'

export default class FragNavi extends DefinedComponent {

    constructor(props) {
        super(props)
        this.editBorder = this.$('editBorder')
        this.text = this.$('text')
    }

    render() {
        return (
            <linear style={styles.root}>
                <frame id="editBorder" style={styles.editBorder}>
                    <edit id='edit' style={styles.edit}/>
                </frame>

                <text id='text' style={styles.edit}/>
            </linear>
        )
    }

    onMount() {
        super.onMount();

        this.$('edit').onTextChanged = (text) => {
            this.text.text = text

            if (text.length > 5) {
                this.editBorder.borderColor = 'red'
                this.text.borderColor = 'red'
            } else {
                this.editBorder.borderColor = '#aaaaaa'
                this.text.borderColor = '#aaaaaa'
            }
        }
    }

    onShow() {
        localStorage.of('user').setItem('myKey', {
            text: '导航'
        })
    }
}

const styles = {
    root: {
        layoutWidth: 'match',
        layoutHeight: 'match',
        padding: 30,
        orientation: 'vertical'
    },
    editBorder: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        borderRadius: 10,
        borderColor: '#aaaaaa',
        borderWidth: 0.5,
    },
    edit: {
        layoutWidth: 'match',
        layoutHeight: 'wrap',
        textColor: 'black',
        textSize: 16,
        hintColor: '#999999',
        hint: '请输入文字',
        borderRadius: 10,
        borderColor: '#aaaaaa',
        borderWidth: 0.5,
        padding: 10,
        textStyle: 'underline',
        singleLine: true
    }
}
