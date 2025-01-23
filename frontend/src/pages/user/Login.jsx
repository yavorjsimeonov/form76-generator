import React from 'react';
import LoginForm from "../../components/LogInForm";
import Header from "../../components/Header";
import Footer from "../../components/Footer";

function LoginPage() {
    return (
        <div className="login-page">
            <Header/>
            <LoginForm />
            <Footer />
        </div>
    );
}

export default LoginPage;