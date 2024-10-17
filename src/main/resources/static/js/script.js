
const defaultLeagueId = '1';
console.log('adddd');

const leagueId = localStorage.getItem('leagueId');

if (!leagueId || !Number.isInteger(Number(leagueId))) {
    localStorage.setItem('leagueId', defaultLeagueId);
    console.log(`Значение лиги установлено по умолчанию: ${defaultLeagueId}`);
} else {
    console.log(`Текущее значение лиги: ${leagueId}`);
}

function toggleQuestions() {
    const questionColumns = document.querySelectorAll('.questionColumn');
    questionColumns.forEach(column => {
        column.style.display = (column.style.display === 'none') ? '' : 'none';
    }
    );
}
document.addEventListener('DOMContentLoaded', () => {

    function updateNavbarWithLeagueId() {
        console.log('update');
        const leagueId = localStorage.getItem('leagueId');

        if (leagueId) {
            const resultsLink = document.querySelector('.results-link');
            const teamsLink = document.querySelector('.teams-link');
            const tournamentsLink = document.querySelector('.tournaments-link');

            if (resultsLink) {
                resultsLink.href = `/leagues/${leagueId}/results`;
            }
            if (teamsLink) {
                teamsLink.href = `/leagues/${leagueId}/teams`;
            }
            if(tournamentsLink){
                tournamentsLink.href  = `/leagues/${leagueId}/tournaments`
            }
        } else {
            alert('Пожалуйста, выберите лигу!');
        }
    }


    updateNavbarWithLeagueId();
    if (window.location.pathname.match(/^\/leagues\/\d+\/teams$/)){


    document.getElementById('teamForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const teamData = {
            name: document.getElementById('teamName').value,
            university: document.getElementById('university').value,
            leagueId: parseInt(document.getElementById('leagueId').value)
        };

        fetch('/api/teams', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(teamData)
        })
            .then(response => {
            if (!response.ok) {
                console.log(teamData);
                console.log(response);
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
            .then(data => {
            console.log('Success:', data);
            alert('Команда добавлена успешно!');
            location.reload();

        })
            .catch(error => {
            console.error('Error:', error);
            alert('Произошла ошибка при добавлении команды.');
        });
    });
        document.getElementById('teamSiteForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const idSite = document.getElementById('idSite').value
            const leagueId = document.getElementById('leagueIdSite').value;
            const requestData = {
                idSite: idSite,
                leagueId: leagueId
            };
            fetch('/site/teams',{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
                .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Команда добавлена успешно!');
                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при добавлении команды');
            });
        });
    }
    if (window.location.pathname.match(/^\/leagues\/\d+\/teams\/\d+\/players\/\d+$/)) {


        document.getElementById('transferForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const transferData = {
                oldTeamId: document.getElementById('oldTeamId').value,
                newTeamId: document.getElementById('newTeamId').value,
                playerId: document.getElementById('playerId').value,
                transferDate: document.getElementById('transferDate').value,
                comment: document.getElementById('comment').value
            };

            fetch('/api/transfers', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(transferData)
            })
                .then(response => {
                if (!response.ok) {
                    console.log(transferData);
                    console.log(response);
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Трансфер произведен!');
                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при проведении трансфера');
            });
        });
    }
    if (window.location.pathname.match(/^\/leagues\/\d+\/teams\/\d+$/)){
        document.getElementById('playerForm').addEventListener('submit', function(event) {
            event.preventDefault();

            const playerData = {
                name: document.getElementById('playerName').value,
                patronymic: document.getElementById('playerPatronymic').value,
                surname:document.getElementById('playerSurname').value,
                university:document.getElementById('playerUniversity').value,
                dateOfBirth:document.getElementById('playerDateBirth').value,
                university: document.getElementById('playerUniversity').value,
                teams: [parseInt(document.getElementById('teamId').value)]
            };
            console.log(playerData);
            const teamId = document.getElementById('teamId');
            fetch('/api/players', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(playerData)
            })
                .then(response => {
                if (!response.ok) {
                    console.log(playerData);
                    console.log(response);
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Игрок добавлена успешно!');

                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при добавлении команды.');
            });
        });

    }
    if (window.location.pathname.match(/^\/leagues\/\d+\/tournaments$/))
    {
        document.getElementById('tournamentNewForm').addEventListener('submit', function(event) {
            event.preventDefault();


            const tournamentData = {
                name: document.getElementById('name').value,
                idSite: document.getElementById('idSite').value,
                dateOfStart:document.getElementById('dateOfStart').value,
                dateOfFinal:document.getElementById('dateOfFinal').value,
                leagueIds: [document.getElementById('leagueId').value]

            };
            console.log(tournamentData);
            //const leagueId = document.getElementById('leagueId');
            fetch('/api/tournaments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(tournamentData)
            })
                .then(response => {
                if (!response.ok) {
                    console.log(tournamentData);
                    console.log(response);
                    console.log(response.json())
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Турнир добавлен успешно!');

                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при добавлении турнира');
            });
        });
        document.getElementById('tournamentForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const tournamentId = document.getElementById('tournamentId').value
            const leagueId = document.getElementById('leagueId').value;
            fetch('/api/leagues/'+leagueId+'/tournaments/'+tournamentId, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(tournamentId)
            })
                .then(response => {
                if (!response.ok) {
                    console.log(response);
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Турнир добавлен успешно!');

                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при добавлении турнира');
            });
        });
        document.getElementById('tournamentSiteForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const idSite = document.getElementById('idSite').value
            const leagueId = document.getElementById('leagueIdSite').value;
            const requestData = {
                idSite: idSite,
                leagueIds: [leagueId]
            };
            fetch('/site/tournaments',{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
                .then(response => {
                if (!response.ok) {
                    console.log(tournamentId);
                    console.log(leagueId);
                    console.log(response);
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
                .then(data => {
                console.log('Success:', data);
                alert('Турнир добавлен успешно!');

                location.reload();

            })
                .catch(error => {
                console.error('Error:', error);
                alert('Произошла ошибка при добавлении турнира');
            });
        });

    }
    var modal = document.getElementById("myModal");

    var btn = document.getElementById("openModal");

    var span = document.getElementsByClassName("close")[0];

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }    }
    const leagueLinks = document.querySelectorAll('.league-link');
    leagueLinks.forEach(link => {
        link.addEventListener('click', (event) => {

            const leagueId = event.currentTarget.getAttribute('data-league-id');
            if (leagueId) {
                console.log('update league', leagueId);
                localStorage.setItem('leagueId', leagueId);
                updateNavbarWithLeagueId();
            }
        });
    });
});

const body = document.querySelector("body");
const darkLight = document.querySelector("#darkLight");
const sidebar = document.querySelector(".sidebar");
const submenuItems = document.querySelectorAll(".submenu_item");
const sidebarOpen = document.querySelector("#sidebarOpen");
const sidebarClose = document.querySelector(".collapse_sidebar");
const sidebarExpand = document.querySelector(".expand_sidebar");
sidebarClose.addEventListener("click", () => {
    sidebar.classList.add("close", "hoverable");
});
sidebarExpand.addEventListener("click", () => {
    sidebar.classList.remove("close", "hoverable");
});
sidebar.addEventListener("mouseenter", () => {
    if (sidebar.classList.contains("hoverable")) {
        sidebar.classList.remove("close");
    }
});
sidebar.addEventListener("mouseleave", () => {
    if (sidebar.classList.contains("hoverable")) {
        sidebar.classList.add("close");
    }
});
darkLight.addEventListener("click", () => {
    body.classList.toggle("dark");
    if (body.classList.contains("dark")) {
        document.setI
        darkLight.classList.replace("bx-sun", "bx-moon");
    } else {
        darkLight.classList.replace("bx-moon", "bx-sun");
    }
});
submenuItems.forEach((item, index) => {
    item.addEventListener("click", () => {
        item.classList.toggle("show_submenu");
        submenuItems.forEach((item2, index2) => {
            if (index !== index2) {
                item2.classList.remove("show_submenu");
            }
        });
    });
});
if (window.innerWidth < 768) {
    sidebar.classList.add("close");
} else {
    sidebar.classList.remove("close");
}






