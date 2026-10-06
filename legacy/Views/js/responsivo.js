$(function () {
    var toggle = document.querySelector('.menu-btn');
    var menu = document.querySelector('.menu');
    if (!toggle || !menu) return;
    var narrow = window.matchMedia('(max-width: 1023px)');
    function setOpen(open) {
        document.body.classList.toggle('atena-menu-aberto', open);
        toggle.setAttribute('aria-expanded', String(open));
        menu.inert = !open;
        if (narrow.matches && open) menu.querySelector('a')?.focus();
    }
    function reset() { setOpen(!narrow.matches); }
    toggle.addEventListener('click', function () {
        setOpen(toggle.getAttribute('aria-expanded') !== 'true');
    });
    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' && toggle.getAttribute('aria-expanded') === 'true') {
            setOpen(false);
            toggle.focus();
        }
    });
    narrow.addEventListener('change', reset);
    reset();
    document.querySelectorAll('.content table').forEach(function (table) {
        var wrapper = document.createElement('div');
        wrapper.className = 'atena-tabela';
        wrapper.tabIndex = 0;
        wrapper.setAttribute('role', 'region');
        wrapper.setAttribute('aria-label', 'Tabela — role horizontalmente para consultar todas as colunas');
        table.before(wrapper);
        wrapper.appendChild(table);
    });
});
