export enum DurationUnit {
    SECONDS = "секунды",
    MINUTES = "минуты",
    HOURS = "часы",
    DAYS = "дни",
    MONTHS = "месяцы",
}


export type Duration = {
    value: number;
    unit: DurationUnit;
}


export function toIsoDuration(duration: Duration): string {
    switch (duration.unit) {
        case DurationUnit.SECONDS:
            return `PT${duration.value}S`;
        case DurationUnit.MINUTES:
            return `PT${duration.value}M`;
        case DurationUnit.HOURS:
            return `PT${duration.value}H`;
        case DurationUnit.DAYS:
            return `P${duration.value}D`;
        case DurationUnit.MONTHS:
            return `P${duration.value}M`;
    }
}


export function fromIsoDuration(iso: string): Duration {
    const match = new RegExp(/^PT?(\d+)([SMHD])$/).exec(iso);

    if (!match) {
        throw new Error(`Unsupported ISO duration: ${iso}`);
    }

    const [, value, unit] = match;

    switch (unit) {
        case "S":
            return {
                value: Number(value),
                unit: DurationUnit.SECONDS,
            };
        case "H":
            return {
                value: Number(value),
                unit: DurationUnit.HOURS,
            };
        case "D":
            return {
                value: Number(value),
                unit: DurationUnit.DAYS,
            };
        case "M":
            return iso.startsWith("PT")
                ? {
                    value: Number(value),
                    unit: DurationUnit.MINUTES,
                }
                : {
                    value: Number(value),
                    unit: DurationUnit.MONTHS,
                };
        default:
            throw new Error(`Unsupported ISO duration: ${iso}`);
    }
}
