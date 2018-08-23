from flask import Flask, jsonify, request
import pymssql
import datetime

app = Flask(__name__)

sensitiveFile = open('sensitive.txt', 'r')
# endline character removed
server=sensitiveFile.readline()[:-1]
user=sensitiveFile.readline()[:-1]
password=sensitiveFile.readline()[:-1]
database=sensitiveFile.readline()[:-1]

@app.route('/player/<path:path>')
def player(path):
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            cursor.execute('SELECT FirstName, LastName, Number FROM Players WHERE ID = \'%s\';', path)
            row = cursor.fetchone()
            rows = []
            while row:
                rows.append(row)
                row = cursor.fetchone()
            return jsonify(rows)

@app.route('/tryout/<path:path>')
def tryout(path):
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            cursor.execute('SELECT Players.FirstName, Players.LastName, Players.ID FROM Players INNER JOIN PlayerTryouts ON Players.ID=PlayerTryouts.PlayerID AND PlayerTryouts.TryoutID = %s;', path)
            row = cursor.fetchone()
            rows = []
            while row:
                rows.append(row)
                row = cursor.fetchone()
            return jsonify(rows)

@app.route('/timedEval', methods = ['POST'])
def saveTimedEval():
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            print(request.is_json)
            content = request.get_json()
            print(content)
            ID = content.get('ID')
            duration = content.get('duration')
            currentTime = datetime.datetime.now().strftime('%Y%m%d %H:%M:%S')
            cursor.execute('INSERT INTO TimedEvaluations(PlayerID, Duration, Date) VALUES (%d, %d, %s);', (ID, duration, currentTime))
            conn.commit()
            return 'Success'


if __name__ == '__main__':
    #app.run(debug=True) # localhost
    app.run(host='192.168.0.160',debug=True)