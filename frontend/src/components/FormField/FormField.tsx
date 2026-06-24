import React from "react";
import styles from "./FormField.module.css";
import {Info} from "lucide-react";

type FormFieldProps = {
    className?: string;
    label: string;
    htmlFor: string;
    error?: string | null;
    children: React.ReactNode
}

export default function FormField({className, label, htmlFor, error, children}: Readonly<FormFieldProps>) {
    return (
        <div className={`${styles.formField} ${className ?? ""}`}>
            <label htmlFor={htmlFor}>
                {label}
            </label>
            {children}
            {
                error &&
                <div className={styles.formFieldError}>
                    <Info height={17} width={17}/>
                    <span>{error}</span>
                </div>
            }
        </div>
    );
}
