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
            playerID = int(content.get('playerID'))
            tryoutID = content.get('tryoutID')
            duration = content.get('duration')
            currentTime = datetime.datetime.now().strftime('%Y%m%d %H:%M:%S')
            cursor.execute('INSERT INTO TimedEvaluations(PlayerID, TryoutID, Duration, Date) VALUES (%d, %d, %d, %s);', (playerID, tryoutID, duration, currentTime))
            conn.commit()
            return jsonify({'Success' : 1})

@app.route('/gameEval', methods = ['POST'])
def saveGameEval():
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            print(request.is_json)
            content = request.get_json()
            print(content)
            playerID = int(content.get('playerID'))
            tryoutID = content.get('tryoutID')
            speed = int(content.get('speed'))
            hockeyAwareness = int(content.get('hockeyAwareness'))
            competeLevel = int(content.get('competeLevel'))
            puckHandling = int(content.get('puckHandling'))
            agility = int(content.get('agility'))
            currentTime = datetime.datetime.now().strftime('%Y%m%d %H:%M:%S')
            cursor.execute('INSERT INTO SkillEvaluations(PlayerID, TryoutID, Speed, HockeyAwareness, CompeteLevel, PuckHandling, Agility, Date) VALUES (%d, %d, %d, %d, %d, %d, %d, %s);', (playerID, tryoutID, speed, hockeyAwareness, competeLevel, puckHandling, agility, currentTime))
            conn.commit()
            return jsonify({'Success' : 1})


if __name__ == '__main__':
    #app.run(debug=True) # localhost
    app.run(host='192.168.0.160',debug=True)