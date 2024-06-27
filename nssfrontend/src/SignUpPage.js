import React from 'react';
import './Auth.css'; // Общий CSS файл для стилей авторизации
//import logo from './img/LogoOneTask.png'; // Импорт логотипа
import sideImage from './img/loginimage.jpeg';
import { Link } from 'react-router-dom';


const SignUpPage = () => {
    return (
        <div className="main-container">
            <div className="content-container">
                <div className="form-container">
                    <h1>Sign Up</h1>
                    <form>
                        <div className="input-group">
                            <label htmlFor="username" className="label">Username</label>
                            <input type="text" id="username" placeholder="Enter your username" required/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="firstname" className="label">First Name</label>
                            <input type="text" id="firstname" placeholder="Enter your first name" required/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="lastname" className="label">Last Name</label>
                            <input type="text" id="lastname" placeholder="Enter your last name" required/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="email" className="label">E-mail</label>
                            <input type="email" id="email" placeholder="Enter your E-mail address" required/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="password" className="label">Enter your password</label>
                            <input type="password" id="password" placeholder="Password" required/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="confirm-password" className="label">Confirm your password</label>
                            <input type="password" id="confirm-password" placeholder="Confirm Password" required/>
                            <p className="login-prompt">
                                If you have an account you can <Link to="/login">login here</Link>
                            </p>

                        </div>
                        <button type="submit" className="signup-button">Sign Up</button>
                    </form>
                </div>
            </div>
            <div className="image-container">
                <img src={sideImage} alt="Side Image" className="side-image"/>

            </div>
        </div>
    );
};

export default SignUpPage;
