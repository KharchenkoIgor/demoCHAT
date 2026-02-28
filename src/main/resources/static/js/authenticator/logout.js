async function logout() {
    try {
        const response = await fetch('/logout', {
            method: 'POST'
        });

        if (response.ok) {
            window.location.href = "/index.html";
        } else {
            alert("ログアウト失敗です")
        }
    } catch (error) {
        console.error("ログアウトのエラー", error);
    }
}