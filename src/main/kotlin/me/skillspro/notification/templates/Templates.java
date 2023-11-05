package me.skillspro.notification.templates;

public class Templates {
    public static final String accountVerification = """
            <html>
                            <h4>Welcome to Skillspro</h4>
                             <p>To verify your account, use the token: <b>${token}</b></p>
                        </html>
            """.stripIndent().strip();
}
