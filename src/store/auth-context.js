import React, { useState, useEffect } from "react";

const AuthContext = React.createContext({
    isloggedIn: false,
    onLogout: () => {},
    onLogin: (email, password) => {}
});

export const AuthContextProvider = (props) => {
    const [isloggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const storedUserLoggedInInformation = localStorage.getItem('isLoggedIn');
    
        if (storedUserLoggedInInformation === '1') {
          setIsLoggedIn(true);
        }
      }, []);

    const logoutHandler = () => {
        localStorage.removeItem('isLoggedIn');
        setIsLoggedIn(false);
    }
    const loginHandler = () => {
        localStorage.setItem('isLoggedIn', '1');
        setIsLoggedIn(true);
    }

    return (<AuthContext.Provider
        value={{
            isloggedIn: isloggedIn,
            onLogout: logoutHandler,
            onLogin: loginHandler
        }}>
        {props.children}
    </AuthContext.Provider>);
};

export default AuthContext;

