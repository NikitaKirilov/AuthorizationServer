import React, {forwardRef} from "react";
import styles from "./Button.module.css";

type ButtonProps =
    React.ButtonHTMLAttributes<HTMLButtonElement> & {
    className?: string
    variant?: "primary" | "secondary" | "danger";
};

export const Button =
    forwardRef<HTMLButtonElement, ButtonProps>(
        ({className, variant = "primary", ...props}, ref) => {
            return <button
                ref={ref}
                className={`${styles.button} ${styles[variant]} ${className || ""}`}
                {...props}
            />;
        });

