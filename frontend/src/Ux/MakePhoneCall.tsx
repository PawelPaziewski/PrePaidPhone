import React, {Component} from "react";

export class MakePhoneCall extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            valueCallerNumber: '',
            valueReceiverNumber: '',
            valueCallDuration: '',
            isResponse: false,
            errorState: false,
            data: {}
        };

        this.getCallerPhoneNumber = this.getCallerPhoneNumber.bind(this);
        this.getReceiverPhoneNumber = this.getReceiverPhoneNumber.bind(this);
        this.getCallDuration = this.getCallDuration.bind(this);
    }

    private getCallerPhoneNumber(event: any): void {
        this.setState({valueCallerNumber: event.target.value, isResponse: false, errorState: false});
    }

    private getReceiverPhoneNumber(event: any): void {
        this.setState({valueReceiverNumber: event.target.value, isResponse: false, errorState: false});
    }

    private getCallDuration(event: any): void {
        this.setState({valueCallDuration: event.target.value, isResponse: false, errorState: false});
    }

    private putData(): any {
        const url: any = `http://localhost:8080/makePhoneCall?callerPhoneNumber=${this.state.valueCallerNumber}&receiverPhoneNumber=${this.state.valueReceiverNumber}&callDuration=PT${this.state.valueCallDuration}`;
        const requestOptions = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        };
        fetch(url, requestOptions)
            .then(response => { if(response.ok){
                this.setState({isResponse: true});
                return response.text();
            }else{
                this.setState({errorState : true});
            }
            });
    }

    render() {
        const success : String = this.state.isResponse ? "success" : " ";
        const error : String = this.state.errorState ? "error" : " ";

        return (<>
                <div className={`modal ${error} ${success}`}>
                    <h3>Połączenie</h3>
                    <div className={`modal-inside`}>
                        <form>
                            <input type="text" placeholder={`Z numery`} title={"Spróbuj '111 111 111'"} onChange={this.getCallerPhoneNumber}/>
                            <input type="text" placeholder={`Na numer`} title={"Spróbuj '111 111 112'"} onChange={this.getReceiverPhoneNumber}/>
                            <input type="text" placeholder={`Czas trwania połączenia`} title={"Spróbuj '10H', '10M' lub '10S'"} onChange={this.getCallDuration}/>
                            <span className={"hint"}>W celu podpowiedzi najedź myszką na pole</span>
                            <input type="reset"/>
                        </form>
                        <button onClick={() => this.putData()}>ZADZWOŃ</button>
                    </div>
                </div>
            </>
        );
    }
}