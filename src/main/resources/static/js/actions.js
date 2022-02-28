/*
func: Show password
 */
function showPassword() {
    var x = document.getElementById("myInput");
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}
/*
func: image profile.
 */
const imgDiv = document.querySelector('.personal-photo');
const img = document.querySelector('#photo');
const avatar = document.querySelector("#avatar");
const file = document.querySelector('#file');
const uploadBtn = document.querySelector('#uploadBtn');

imgDiv.addEventListener('mouseenter', function(){
    uploadBtn.style.display = "block";
});


imgDiv.addEventListener('mouseleave', function(){
    uploadBtn.style.display = "none";
});

file.addEventListener('change', function (){
    const choosedFile = this.files[0];

    if(choosedFile){
        const reader = new FileReader();

        reader.addEventListener('load', function (){
            avatar.setAttribute('src', reader.result);
        })
        reader.addEventListener('load', function (){
            img.setAttribute('src', reader.result);
        })
        reader.readAsDataURL(choosedFile);

        avatar.addEventListener("click", () => {
            avatar.remove();
        });
        img.addEventListener("click", () => {
            img.remove();
        });
    }
});

