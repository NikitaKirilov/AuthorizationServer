import React from "react";
import {Link} from "react-router-dom";
import "./NavBarOption.css";

type NavBarOptionProps = {
    url: string;
    text: string;
    isSelected?: boolean,
    children: React.ReactNode,
}

export default function NavBarOption({url, text, isSelected, children}: Readonly<NavBarOptionProps>) {
    return (
        <Link to={url} className={isSelected ? "nav-bar-option nav-bar-option-current" : "nav-bar-option"}>
            {children}
            <p>{text}</p>
        </Link>
    );
}
