import React, {Component} from "react";

export class Billing extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueNumber: "",
            data: [],
            isResponse: false,
            error : false
        };

        this.getNumber = this.getNumber.bind(this);
    }

    private getNumber(event: any): void {
        this.setState({valueNumber: event.target.value, error : false, isResponse: false});
    }

    private getBilling(): any {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'application/json'},

        };
        fetch(`http://localhost:8080/billing?phoneNumber=${this.state.valueNumber}`, requestOptions)
            .then(response => response.json())
            .then(data => this.setState({data: data, isResponse: true}))
            .catch(
                error => this.setState({error : true})
            );
    }

    public parseDate(inData: any): string {
        const dateRegExp: RegExp = new RegExp('^[0-9]{4}-((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01])|(0[469]|11)-(0[1-9]|[12][0-9]|30)|(02)-(0[1-9]|[12][0-9]))T(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])?.[0-9]{0,7}$', 'g');
        if (typeof inData === 'string') {
            if (dateRegExp.test(inData)) {
                return new Intl.DateTimeFormat('de-DE', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                }).format(new Date(inData)).toString();
            } else {
                return inData;
            }
        } else {
            return inData.toString();
        }
    }
    render() {
        const dataList: any[] = this.state.data;
        const error : String = this.state.error ? "error" : " ";
        return (<>
                <div className={`modal ${error}`}>
                    <h3>Billing</h3>
                    <div className={`modal-inside`}>
                        <p>Dla jakiego numeru chciałbyś billing?</p>
                        <form>
                            <input type="text" placeholder={`Wprowadź numer telefonu`} title={"Spróbuj '111 111 111'"}
                                   onChange={this.getNumber}/>
                            <span className={"hint"}>W celu podpowiedzi najedź myszką na pole</span>
                            <input type="reset"/>
                        </form>
                        <button onClick={() => this.getBilling()}>SPRAWDŹ</button>
                    </div>
                </div>
                {this.state.isResponse ?
                    <div className={`modal billing`}>
                        <div className={`modal-inside billing`}>
                            <div className={`row title`}>
                                <span className={`date`}>Date</span>
                                <span className={`type`}>Type</span>
                                <span className={`duration`}>Duration</span>
                                <span className={`cost`}>Cost</span>
                            </div>
                            {
                                dataList.map((element, i) => {
                                    return (<>
                                            <div className={`row`} key={`key-${i}`}>
                                                <span className={`date`} key={`key-date-${i}`}>{this.parseDate(element.timestamp.date)}</span>
                                                <span className={`type`} key={`key-type-${i}`}>{element.type}</span>
                                                <span className={`duration`} key={`key-duration-${i}`}>{element.duration !== null ? element.duration : ""}</span>
                                                <span className={`cost`} key={`key-cost-${i}`}>{element.cost.amount + element.cost.currency}</span>
                                            </div>
                                        </>
                                    );
                                })
                            }
                        </div>
                    </div>
                    :
                    <></>
                }
            </>
        );
    }
}