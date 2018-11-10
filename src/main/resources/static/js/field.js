function orgChange() {

    var agrDiv=document.getElementById("agreementField");

    agrDiv.style.display=getRealDisplay(agrDiv)=='none'?'block':'none';

}

function isAdmin(){
    if(document.getElementById("ADMINLabel")!=null){
        document.getElementById("adminDiv").style.display = 'flex'
    }
}


var currentShowed = null;

function showLocation() {
    var currentId = creationForm.locationId.options[creationForm.locationId.selectedIndex].value;

    if(currentShowed != null)
        currentShowed.style.display=getRealDisplay(currentShowed)=='none'?'block':'none';

    var locationDiv = document.getElementById("location"+currentId);
    locationDiv.style.display=getRealDisplay(locationDiv)=='none'?'block':'none';
    currentShowed = locationDiv;
}


function getRealDisplay(elem) {
    if (elem.currentStyle) {
        return elem.currentStyle.display
    } else if (window.getComputedStyle) {
        var computedStyle = window.getComputedStyle(elem, null)

        return computedStyle.getPropertyValue('display')

    }
}

function checkAmount(element){
    var avaible = document.getElementById('avaibleAmount'+element.id.substring('amount'.length));
    if(element.value>=avaible.innerText)
        element.value = avaible.innerText;
    else if(element.value<0)
        element.value
    else
        true;
}

function enableField(element) {
    var amount = document.getElementById('amount' + element.id.substring('attribute'.length));
    if (element.checked) {
        amount.disabled = false;
        amount.value = 0;
    }
    else {
        amount.disabled = true;
        amount.value = null;
    }
}

function checkPayment(total){
    var cost = document.getElementById("cost");
    if(total.value>=cost.innerText){
        total.value=cost.innerText;
    }else if(total.value<0){
        total.value=0;
    }
}

function showAttributes(reconstructionDiv){
    hideAll();
    reconstructionDiv.classList.add("active");
    var list = document.getElementsByClassName("to-rec"+reconstructionDiv.id.substring('reconstruction'.length));
    for(var i=0; i<list.length; i++){
        list[i].style.display='flex'
    }
}
function hideAll() {

    var list = document.getElementsByClassName("to-hide");
    for(var i=0; i<list.length; i++){
        list[i].style.display='none'
    }
    var list = document.getElementsByClassName("list-item");
    for(var i=0; i<list.length; i++){
        list[i].classList.remove("active")
    }
}