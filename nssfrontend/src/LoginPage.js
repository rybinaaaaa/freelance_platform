import React from 'react';
import './Auth2.css'; // Общий CSS файл для стилей авторизации

const LoginPage = () => {
    console.log('Компонент логин загружен');
    return (
        <div className="container">
            <div className="form-container">
                <h2>Login</h2>
                <form>
                    <div className="input-group">
                        <input type="text" id="login" placeholder="Login" required />
                    </div>
                    <div className="input-group">
                        <input type="password" id="password" placeholder="Password" required />
                    </div>
                    <div className="buttons">
                        <a href="#" className="forgot-password">Forgot password</a>
                        <button type="submit" className="login-button">Login</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
