import React from "react";
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import {AuthProvider, useAuth} from './components/AuthContext'
import LoginPage from "./pages/user/Login";
import PrivateRoute from "./components/PrivateRoute";
import HomePage from "./pages/Home"
import AdministrationsPage from "./pages/admin/Administrations";
import ReportsPage from "./pages/admin/Reports";
import UsersPage from "./pages/user/Users";
import AdministrationPage from "./pages/admin/Administration";
import LocationDetailsPage from "./pages/admin/Location";

function App() {

    return (
        <>
            <AuthProvider>
                <Router>
                    <div className="App">
                        <Routes>
                            {/* Public Routes */}
                            <Route exact path="/" element={<LoginPage />} />
                            <Route exact path="login" element={<LoginPage />} />

                            {/* Protected Routes */}
                            <Route exact path="home" element={<PrivateRoute><HomePage /></PrivateRoute>} />
                            <Route exact path="administrations" element={<PrivateRoute><AdministrationsPage /></PrivateRoute>} />
                            <Route exact path="administrations/:id" element={<PrivateRoute><AdministrationPage /></PrivateRoute>} />
                            <Route exact path="locations/:id" element={<PrivateRoute><LocationDetailsPage /></PrivateRoute>} />
                            <Route exact path="reports" element={<PrivateRoute><ReportsPage /></PrivateRoute>} />
                            <Route exact path="users" element={<PrivateRoute><UsersPage /></PrivateRoute>} />

                            {/* Catch-All Route */}
                            <Route path="*" element={<Navigate to="/login" />} />
                        </Routes>
                    </div>
                </Router>
            </AuthProvider>
        </>
    );
}

export default App;