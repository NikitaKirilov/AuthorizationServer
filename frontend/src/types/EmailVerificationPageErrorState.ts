export interface EmailVerificationPageErrorState {
    emailVerificationCodeValidationError: string | null,
    emailVerificationCodeCooldownError: string | null,
    userError: string | null,
}
