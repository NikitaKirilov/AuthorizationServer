export const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
export const PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

export const validateEmail = (email: string): string | null =>
    EMAIL_REGEX.test(email) ? null : "Email must be correct";

export const validatePassword = (password: string): string | null =>
    PASSWORD_REGEX.test(password) ? null : "Password must be at least 8 symbols long and contain 1 letter and 1 digit";

export const checkFieldNotEmpty = (name: string): string | null =>
    name ? null : "This field must not be empty";

