import {NavLink, NavLinkProps} from "react-router-dom";
import {forwardRef} from "react";
import styles from "./Link.module.css";

interface LinkProps extends NavLinkProps {
    to: string;
    className?: string;
}

const Link =
    forwardRef<HTMLAnchorElement, LinkProps>(
        ({to, className, ...props}, ref) => (
            <NavLink ref={ref} {...props} className={`${styles.link} ${className || ""}`} to={to}/>
        ),
    );

export default Link;
