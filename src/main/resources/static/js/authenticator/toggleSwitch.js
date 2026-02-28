const toggle = document.getElementById('modeToggle');
const formTitle = document.getElementById('form-title');

function updateForm() {

    const isReg = toggle.checked;

    formTitle.innerText = isReg ? "新登録" : "ログイン";

    const email = document.getElementById('email');
    const confirm = document.getElementById('confirmPassword');
    const errEmail = document.getElementById('error-email');
    const errConfirm = document.getElementById('error-confirmPassword');

    if (isReg) {
        [email, confirm, errEmail, errConfirm].forEach(el => el.classList.remove('hidden-field'));
    } else {
        [email, confirm, errEmail, errConfirm].forEach(el => el.classList.add('hidden-field'));
        clearErrors();
    }
}

toggle.addEventListener('change', updateForm);

updateForm();