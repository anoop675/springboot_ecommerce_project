from flask import Flask, request, jsonify
from web3 import Web3
from eth_account import Account

app = Flask(__name__)

@app.route('/connect', methods=['POST'])
def connect_to_infura():
    try:
        # Parse the JSON body to get the Infura project ID
        data = request.get_json()
        if not data or 'infura_project_id' not in data:
            return jsonify({"error": "Missing 'infura_project_id' in request body"}), 400

        infura_project_id = data['infura_project_id']

        # Connect to the Ethereum node via Infura
        web3 = Web3(Web3.HTTPProvider(f"https://sepolia.infura.io/v3/{infura_project_id}"))

        # Check if the connection to the node was successful
        if web3.is_connected():
            return jsonify({"message": "Successfully connected to Infura."}), 200
        else:
            return jsonify({"error": "Failed to connect to Infura."}), 500

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/get-wallet', methods=['POST'])
def create_and_send_wallet():
    try:
        # Parse the JSON body to get the Infura project ID
        data = request.get_json()
        if not data or 'infura_project_id' not in data:
            return jsonify({"error": "Missing 'infura_project_id' in request body"}), 400

        infura_project_id = data['infura_project_id']

        # Connect to the Ethereum node via Infura
        web3 = Web3(Web3.HTTPProvider(f"https://sepolia.infura.io/v3/{infura_project_id}"))

        # Generate a new Ethereum account
        account = web3.eth.account.create()

        # Check if account is created successfully
        if account and account.address and account._private_key:
            return jsonify({"sender_address": account.address, "sender_private_key": account._private_key.hex()}), 200
        else:
            return jsonify({"error": "Failed to generate wallet credentials"}), 500
            
    except Exception as e:
        return jsonify({"error": str(e)}), 500
            
@app.route('/get-wallet-balance', methods=['POST'])
def get_balance():
    try:
        # Parse the JSON body to get the Infura project ID
        data = request.get_json()
        if not data or 'infura_project_id' not in data:
            return jsonify({"error": "Missing 'infura_project_id' in request body"}), 400
        if not data or 'sender_address' not in data:
            return jsonify({"error": "Missing 'sender_address' in request body"}), 400

        infura_project_id = data['infura_project_id']
        sender_address = data['sender_address']
            
        web3 = Web3(Web3.HTTPProvider(f"https://sepolia.infura.io/v3/{infura_project_id}"))
        
        if not web3.is_connected():
            print("Error: Unable to connect to the Ethereum network.")
            return jsonify({"error": "Cannot connect to Ethereum network"}), 400

        balance = web3.eth.get_balance(sender_address)
        eth_balance = web3.from_wei(balance, 'ether')
        eth_balance_float = float(eth_balance)
        return jsonify({"balance": eth_balance_float}), 200
            
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# Helper functions to validate address and private key
def is_valid_address(address):
    return Web3.is_address(address)

def is_valid_private_key(private_key):
    try:
        Account.from_key(private_key)
        return True
    except ValueError:
        return False

def verify_signature(infura_project_id, sender_private_key, receiver_address):
    # Connect to Sepolia testnet using Infura
    web3 = Web3(Web3.HTTPProvider(f'https://sepolia.infura.io/v3/{infura_project_id}'))

    if not web3.is_connected():
        return jsonify({'error': 'Failed to connect to Ethereum node. Please check your connection.'}), 500

    # Validate sender private key and receiver address
    if not is_valid_private_key(sender_private_key):
        return jsonify({'error': 'Invalid sender private key.'}), 400

    sender_account = Account.from_key(sender_private_key)

    if not is_valid_address(receiver_address):
        return jsonify({'error': 'Invalid receiver address.'}), 400

    try:
        # Transaction details
        transaction = {
            'to': receiver_address,
            'value': web3.to_wei(0.01, 'ether'),  # Sending 0.01 ETH
            'gas': 21000,
            'gasPrice': web3.eth.gas_price,
            'nonce': web3.eth.get_transaction_count(sender_account.address),
            'chainId': web3.eth.chain_id
        }

        # Sign the transaction
        signed_txn = web3.eth.account.sign_transaction(transaction, sender_private_key)

        # Recover the sender's address from the signature
        recovered_address = web3.eth.account.recover_transaction(signed_txn.rawTransaction)

        # Check if the recovered address matches the sender's address
        if recovered_address.lower() == sender_account.address.lower():
            is_valid_signature = True
        else:
            is_valid_signature = False

        '''
        # Check if the sender has sufficient balance
        sender_balance = web3.eth.get_balance(recovered_address)
        has_sufficient_balance = sender_balance >= transaction['value'] + (transaction['gas'] * transaction['gasPrice'])
        
        response = {
            'transaction_hash': web3.to_hex(signed_txn.hash),
            'recovered_address': recovered_address,
            'sender_address': sender_account.address,
            'is_valid_signature': is_valid_signature,
            'has_sufficient_balance': has_sufficient_balance
        }'''

        return is_valid_signature

    except ValueError as e:
        return False
    except Exception as e:
        return False
        

@app.route('/send_transaction', methods=['POST'])
def send_transaction():
    try:
        # Get the data from the request
        data = request.json
        infura_project_id = data['infura_project_id']
        sender_address = data['sender_address']
        private_key = data['private_key']
        eth_amount = data['eth_amount']
        recipient_address = data['recipient_address']
        
        # Infura URL
        infura_url = f"https://sepolia.infura.io/v3/{infura_project_id}"

        # Connect to Infura
        web3 = Web3(Web3.HTTPProvider(infura_url))

        # Check connection
        if not web3.is_connected():
            return jsonify({'error': 'Failed to connect to Infura'}), 500

        # Transaction details
        value_to_send = web3.to_wei(eth_amount, 'ether')
        gas_limit = 21000  # Standard gas limit for ETH transfer
        gas_price = web3.to_wei('50', 'gwei')
        chain_id = 11155111  # Sepolia chain ID

        # Get sender's balance
        balance = web3.eth.get_balance(sender_address)
        total_tx_cost = value_to_send + (gas_limit * gas_price)

        # Check if the balance is sufficient
        if balance < total_tx_cost:
            return jsonify({
                'error': 'Insufficient funds',
                'balance': float(web3.from_wei(balance, 'ether')),
                'required_balance': float(web3.from_wei(total_tx_cost, 'ether'))
            }), 400

        nonce = web3.eth.get_transaction_count(sender_address)
        tx = {
            'nonce': nonce,
            'to': recipient_address,
            'value': value_to_send,
            'gas': gas_limit,
            'gasPrice': gas_price,
            'chainId': chain_id
        }

        # Sign the transaction
        signed_tx = web3.eth.account.sign_transaction(tx, private_key)

        # Send the transaction
        tx_hash = web3.eth.send_raw_transaction(signed_tx.rawTransaction)

        # Wait for the transaction receipt
        tx_receipt = web3.eth.wait_for_transaction_receipt(tx_hash)

        # Check if the transaction was successful and tx signature is verified
        if tx_receipt.status == 1 and verify_signature(infura_project_id, private_key, recipient_address):
            updated_balance = web3.eth.get_balance(sender_address)
            return jsonify({
                'status': 'success',
                'transaction_hash': web3.to_hex(tx_hash),
                'block_number': tx_receipt.blockNumber,
                'updated_balance': float(web3.from_wei(updated_balance, 'ether'))
            })

        else:
            return jsonify({'error': 'Transaction failed'}), 500

    except ValueError as e:
        return jsonify({'error': f'Transaction failed: {str(e)}', 'message': 'Error 400'}), 400
    except Exception as e:
        return jsonify({'error': f'An unexpected error occurred: {str(e)}'}), 500


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0")
