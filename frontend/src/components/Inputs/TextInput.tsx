import React, {forwardRef} from "react";
import styles from "./Input.module.css";

export const TextInput =
    forwardRef<HTMLInputElement, React.InputHTMLAttributes<HTMLInputElement>>(
        ({className, ...props}, ref) => {
            return (
                <input
                    ref={ref}
                    className={`${styles.textInput} ${(className ?? "")}`}
                    {...props}
                />
            );
        });
