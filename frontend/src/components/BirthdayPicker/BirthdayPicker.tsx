import DatePicker from "react-datepicker";
import "./BirthdayPicker.css";
import "../TextInput/TextInput.css";
import {CalendarSearch} from "lucide-react";

type BirthdayPickerProps = {
    selectedDate: Date | null | undefined;
    onChange: (date: Date | null) => void;
}

export default function BirthdayPicker({selectedDate, onChange}: Readonly<BirthdayPickerProps>) {
    return (
        <div className={"text-input-wrapper"}>
            <CalendarSearch className={"datepicker-icon"}/>
            <DatePicker id={"birthday"} name={"birthday"} className={"text-input"}
                        selected={selectedDate} placeholderText={"Выберите дату рождения"}
                        dateFormat={"dd.MM.yyyy"}
                        showYearDropdown
                        scrollableYearDropdown
                        yearDropdownItemNumber={100}
                        onChange={onChange}
                        maxDate={new Date()}
                        portalId={"root"}
                        onKeyDown={(e) => e.preventDefault()}
            />
        </div>
    );
}
