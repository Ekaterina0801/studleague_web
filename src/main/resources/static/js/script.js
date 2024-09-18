// Устанавливаем значение по умолчанию, если его еще нет
const defaultLeagueId = '1';
console.log('adddd');

// Получаем значение leagueId из localStorage
const leagueId = localStorage.getItem('leagueId');

// Проверяем, существует ли leagueId и является ли оно целым числом
if (!leagueId || !Number.isInteger(Number(leagueId))) {
  localStorage.setItem('leagueId', defaultLeagueId); // Устанавливаем значение по умолчанию
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
  //toggleQuestions();
  // Функция для обновления ссылок навбара
  function updateNavbarWithLeagueId() {
    console.log('update');
    const leagueId = localStorage.getItem('leagueId');

    if (leagueId) {
      const resultsLink = document.querySelector('.results-link');
      const teamsLink = document.querySelector('.teams-link');
      const tournamentsLink = document.querySelector('.tournaments-link');

      // Обновляем ссылки с использованием ID лиги
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

  // Вызов функции для обновления навбара
  updateNavbarWithLeagueId();

  // Обработчик клика для изменения лиги
  const leagueLinks = document.querySelectorAll('.league-link');
  leagueLinks.forEach(link => {
    link.addEventListener('click', (event) => {
      // Получаем данные из текущей цели события
      const leagueId = event.currentTarget.getAttribute('data-league-id');
      if (leagueId) {
        console.log('update league', leagueId); // выводим ID для проверки
        localStorage.setItem('leagueId', leagueId); // Обновляем ID в localStorage
        updateNavbarWithLeagueId(); // Обновляем навбар
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






