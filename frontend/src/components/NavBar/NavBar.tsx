import React from "react";
import "./NavBar.css";

type NavBarProps = {
    children: React.ReactNode
}

export default function NavBar({children}: Readonly<NavBarProps>) {
    return (
        <div className={"nav-bar"}>
            {children}
        </div>
    );
}
