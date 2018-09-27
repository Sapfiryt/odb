function orgChange() {

    var agrDiv=document.getElementById("agreementField");

    agrDiv.style.display=getRealDisplay(agrDiv)=='none'?'block':'none';

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
