import React from 'react';
import LoginForm from "../../components/LogInForm";
import Header from "../../components/common/Header";
import Footer from "../../components/common/Footer";

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