package eikona.ui

import kotlinx.html.*

class LoginPage : UIPage {

    override fun HTML.render() {
        head {
            title = "Eikona"
        }
        body {
            div("container") {
                form(method = FormMethod.post) {
                    textInput(name = "username")
                    br()
                    passwordInput(name = "password")
                    br()
                    submitInput()
                }
            }
        }
    }
}