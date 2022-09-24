package eikona.ui.pages

import eikona.ui.templates.DefaultPageTemplate
import kotlinx.html.*

class LoginPage : DefaultPageTemplate {

    override fun BODY.renderBody() {
        div("ui middle aligned center aligned fullpage grid") {
            div("nottoowide column") {
                h2("ui image header") {
//                    img(classes = "image", src = "")
                    div(classes = "content") {
                        text("Log in to your Eikona account")
                    }
                }
                form(classes = "ui large form", method = FormMethod.post) {
                    div(classes = "ui stacked segment") {
                        div(classes = "field") {
                            textInput(name = "username") {
                                attributes["placeholder"] = "Enter your username"
                            }
                        }
                        div(classes = "field") {
                            passwordInput(name = "password") {
                                attributes["placeholder"] = "Password"
                            }

                        }
                        div(classes = "field") {
                            submitInput(classes = "ui fluid violet submit button")
                        }
                    }
                }
                div("ui message") {
                    text("New to Eikona? ")
                    a(href = "/signup") {
                        text("Create an account")
                    }
                }
            }

        }
    }
}