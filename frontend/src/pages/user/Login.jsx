import React from 'react';
import LoginForm from "../../components/LogInForm";
import Header from "../../components/common/Header";
import Footer from "../../components/common/Footer";

function LoginPage() {
    return (
        <div className="d-flex align-items-center
                        justify-content-center vh-100">
            <LoginForm />
        </div>
    );
}

export default LoginPage;