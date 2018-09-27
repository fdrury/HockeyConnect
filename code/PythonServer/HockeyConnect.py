from flask import Flask, jsonify, request
import pymssql
import datetime
import csv
import io

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

@app.route('/getGameEval/<tryout>/<player>')
def loadGameEval(tryout, player):
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            cursor.execute('SELECT Speed, HockeyAwareness, CompeteLevel, PuckHandling, Agility FROM SkillEvaluations WHERE TryoutID = %s AND PlayerID = %s ORDER BY Date DESC;', (tryout, player))
            return jsonify(cursor.fetchone())

@app.route('/postGameEval', methods = ['POST'])
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

@app.route('/uploadPlayers', methods = ['POST'])
def uploadPlayers():
    with pymssql.connect(server, user, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            if 'csvfile' not in request.files:
                print('No file found')
                return 'No file found'
            f = request.files['csvfile']
            if not f:
                return 'No file'
            #fileContents = f.stream.read().decode('utf-8')
            fileContents = io.StringIO(f.stream.read().decode('utf-8'), newline=None)
            
            cursor.execute('SELECT ID FROM Tryouts ORDER BY ID DESC')
            row = cursor.fetchone()
            newTryoutID = row.get('ID', 0) + 1
            cursor.execute('INSERT INTO Tryouts(ID) VALUES(%d);', newTryoutID)

            cursor.execute('SELECT ID FROM Players ORDER BY ID DESC')
            row = cursor.fetchone()
            nextPlayerID = row.get('ID', 0) + 1
            reader = csv.DictReader(fileContents)
            for row in reader:
                rowID = row['ID']
                if rowID == '':
                    rowID = str(nextPlayerID)
                    nextPlayerID += 1
                cursor.execute('INSERT INTO Players(FirstName, LastName, ID) VALUES(%s, %s, %s);', (row['FirstName'], row['LastName'], rowID))
                cursor.execute('INSERT INTO PlayerTryouts(PlayerID, TryoutID) VALUES(%s, %d);', (rowID, newTryoutID))

            
            conn.commit()
            return ('File uploaded successfully. Your Tryout ID is %d' % newTryoutID)


if __name__ == '__main__':
    #app.run(debug=True) # localhost
    app.run(host='192.168.0.160',debug=True)