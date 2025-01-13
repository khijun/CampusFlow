const defaultHeaderOption = {
    minWidth: 100,
    resizable: true,
}

// 그리드를 생성해 반환하는 메서드
function createGrid(element, columns, data, headerOption) {
    const Grid = tui.Grid;
    return new Grid({
        el: element,
        columnOptions: headerOption == null ? defaultHeaderOption : headerOption,
        columns: columns,
        data: data,
        scrollX: true,
        bodyHeight: 400,
        bodyWidth: 1300
    })
}