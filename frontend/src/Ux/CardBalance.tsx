import React, {Component} from "react";

export class CardBalance extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueNumber: '',
            amount: {},
            currency: {},
            isResponse: false,
            error: false,
        };

        this.getNumber = this.getNumber.bind(this);
    }

    private getNumber(event: any): void {
        this.setState({valueNumber: event.target.value, amount: '', isResponse: false, error: false});
    }

    private getCardBalance(): any {
        const requestOptions = {
            method: 'GET',
        };
        fetch(`http://localhost:8080/cardBalance?phoneNumber=${this.state.valueNumber}`, requestOptions)
            .then(response => response.json())
            .then(data => this.setState({amount: data.amount, currency: data.currency, isResponse: true})
            ).catch(
            error => this.setState({error : true})
        );
    }

    render() {
        const success : String = this.state.isResponse ? "success" : " ";
        const error : String = this.state.error ? "error" : " ";
        return (<>
                <div className={`modal ${error}`}>
                    <h3>Saldo Konta</h3>
                    <div className={`modal-inside`}>
                        <p>Dla jakiego numeru chciałbyś sprawdzić saldo?</p>
                        <form>
                            <input type="text" placeholder={`Wprowadź numer telefonu`} title={"Spróbuj '111 111 111'"}
                                   onChange={this.getNumber}/>
                            <span className={"hint"}>W celu podpowiedzi najedź myszką na pole</span>
                            <input type={'reset'}/>
                        </form>
                        <button onClick={() => this.getCardBalance()}>SALDO</button>
                    </div>
                </div>
            {this.state.isResponse ?
                <div className={`modal`}>
                    <div className={`modal-inside sec`}>
                        <div>Numer: <span className={`cardBalance`}>{this.state.valueNumber}</span></div>
                        <div>Stan konta:  <span className={`cardBalance`}>{this.state.amount + " " + this.state.currency}</span> </div>

                    </div>
                </div>
                : <></>}
            </>
        );
    }
}