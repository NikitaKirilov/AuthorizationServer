import {ChangeEvent, MouseEvent, useEffect, useState} from "react";
import userProfileApi from "../../api/userProfileApi.ts";
import {UserDto} from "../../types/UserDto.ts";
import "./UserProfilePage.css";

const TITLE = "Profile";

export default function UserProfilePage() {
    const [user, setUser] = useState<UserDto | null>();
    const [editButtonText, setEditButtonText] = useState<string>("Edit");
    const [isDisabled, setDisabled] = useState<boolean>(true);

    useEffect(() => {
        document.title = TITLE;
        userProfileApi.getCurrentUser()
            .then(response => {
                setUser(response.data as UserDto);
            });
    }, []);

    const editButtonOnClick = async (e: MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        if (isDisabled) {
            setDisabled(false);
            setEditButtonText("Send");
        } else {
            if (user) {
                await userProfileApi.updateUser({
                    nickname: user.nickname,
                    givenName: user.givenName,
                    familyName: user.familyName,
                }).then(response => {
                    setUser(response.data as UserDto);
                });
            }

            setDisabled(true);
            setEditButtonText("Edit");
        }
    };

    const handleChange = async (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setUser(prev => prev ? ({...prev, [name]: value}) : prev);
    };

    return (
        <div className={"profile-page"}>
            <div className={"menu"}>
                <p className={"menu-option"}>Email</p>
                <p className={"menu-option"}>Password</p>
                <p className={"menu-option"}>Sessions</p>
                <p className={"menu-option"}>Logout</p>
            </div>
            <div className={"user-info"}>
                <form>
                    <div className={"row"}>
                        <input name={"nickname"} disabled={isDisabled} className={"profile-input"}
                               placeholder={"Nickname"} value={user ? user.nickname : ""} onChange={handleChange}/>
                        <input name={"givenName"} disabled={isDisabled} className={"profile-input"}
                               placeholder={"Given name"} value={user ? user.givenName : ""} onChange={handleChange}/>
                    </div>
                    <div className={"row"}>
                        <input disabled={isDisabled} className={"profile-input"} placeholder={"Family name"}
                               value={user ? user.familyName : ""} onChange={handleChange}/>
                        <input disabled={isDisabled} className={"profile-input"} placeholder={"Family name"}
                               value={user ? user.familyName : ""} onChange={handleChange}/>
                    </div>
                    <button className={"edit-button"} onClick={editButtonOnClick}>{editButtonText}</button>
                </form>
            </div>
        </div>
    );
}
