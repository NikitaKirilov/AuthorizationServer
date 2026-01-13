import React from "react";
import "./FormInput.css";

type FormInputProps = {
    name: string;
    type?: string;
    placeholder: string;
    value: string;
    error?: string | null;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

export default function FormInput({
                                      name,
                                      type,
                                      placeholder,
                                      value,
                                      error,
                                      onChange,
                                  }: Readonly<FormInputProps>) {
    return (
        <div className="form-field">
            <input
                className={"input"}
                type={type}
                name={name}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
            />
            <div className="default-error">{error}</div>
        </div>
    );
}
