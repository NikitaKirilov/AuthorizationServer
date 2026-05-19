import React from "react";
import "./FormField.css";
import {Info} from "lucide-react";

type FormFieldProps = {
    label: string;
    htmlFor: string;
    error?: string;
    children: React.ReactNode
}

export default function FormField({label, htmlFor, error, children}: Readonly<FormFieldProps>) {
    return (
        <div className={"form-field"}>
            <label htmlFor={htmlFor}>
                {label}
            </label>
            {children}
            {
                error &&
                <div className={"form-field-error"}>
                    <Info height={17} width={17}/>
                    <span>{error}</span>
                </div>
            }
        </div>
    );
}
