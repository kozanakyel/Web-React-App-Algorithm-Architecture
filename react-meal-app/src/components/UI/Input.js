import React from 'react';

import classes from './Input.module.css';

//useRef custom component ile ancak bu sekidle kullanilir
const Input = React.forwardRef((props, ref) => {
    return (
        <div className={classes.input}>
            <label htmlFor={props.input.id}>{props.label}</label>
            <input ref={ref} {...props.input} />
        </div>
    );
});

export default Input;