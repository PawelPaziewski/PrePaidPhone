import React, {Component} from "react";

export class StartPage extends Component<any, any> {
    render() {
        return (<>
                <div className={`modal`}>
                    <h3>Pre Paid Phone</h3>
                    <div className={`modal-inside`}>
                        <p>Witamy w aplikacji, która umożliwia kupno karty, wykonywanie połączeń, sms.
                        Można tu również sprawdzać swój stan konta, jak i doładować konto.
                        W celu sprawdzenia wykonanych połączeń czy sms, zakładka "Billing" wyświetli wszystko.
                        Zapraszamy do zakupu karty :)</p>
                    </div>
                </div>
            </>
        );
    }
}