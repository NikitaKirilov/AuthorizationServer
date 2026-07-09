import React, {forwardRef} from "react";
import inputStyles from "../Input.module.css";
import styles from "./TextArea.module.css";

const TextArea =
    forwardRef<HTMLTextAreaElement, React.TextareaHTMLAttributes<HTMLTextAreaElement>>(
        ({className, ...props}, ref) => {
            return (
                <textarea
                    ref={ref}
                    className={`${inputStyles.textInput} ${styles.textarea} ${(className ?? "")}`}
                    {...props}
                />
            );
        });

export default TextArea;
