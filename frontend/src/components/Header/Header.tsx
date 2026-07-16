import {ComponentPropsWithoutRef, forwardRef, ReactNode} from "react";
import styles from "./Header.module.css";

interface HeaderProps extends ComponentPropsWithoutRef<"div"> {
    title: string;
    subtitle?: string;
    icon?: ReactNode;
    className?: string;
    width?: "lg" | "md" | "sm";
    align?: "left" | "center";
    type?: "primary" | "secondary";
}

const Header = forwardRef<HTMLDivElement, HeaderProps>(
    ({icon, title, subtitle, className, width = "md", align = "left", type = "primary", ...props}, ref) => {
        return (
            <div ref={ref} {...props}
                 className={`${styles.header} ${styles[type]} ${styles[width]} ${className || ""}`}>
                {icon}
                <div className={`${styles.textWrapper} ${styles[align]}`}>
                    <h1 className={styles.title}>{title}</h1>
                    <span className={styles.subtitle}>{subtitle}</span>
                </div>
            </div>
        );
    },
);

export default Header;
