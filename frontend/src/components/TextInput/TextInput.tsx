import React from "react";
import "./TextInput.css";

type TextInputProps = {
    children?: React.ReactNode
    name: string;
    placeholder: string;
    value: string;
    password?: boolean;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function TextInput({children, name, placeholder, value, password, onChange}: Readonly<TextInputProps>) {
    return (
        <div className={"text-input-wrapper"}>
            {children}
            <input id={name} name={name} className={"text-input"}
                   placeholder={placeholder}
                   value={value} type={password && "password" || "text"} onChange={onChange}/>
        </div>
    );
}
