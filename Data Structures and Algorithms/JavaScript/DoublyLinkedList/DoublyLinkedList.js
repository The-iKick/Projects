//import DoublyLinkedListNode from './DoublyLinkedListNode.js';
const DoublyLinkedListNode = require('./DoublyLinkedListNode.js');

module.exports = class DoublyLinkedList {
    constructor () {
        this.head = null;
        this.tail = null;
    }

    insert (value) {
        let node = new DoublyLinkedListNode(value);

        if (this.head == null) {
            this.head = node;
            this.tail = node;
        } else {
            node.prev = this.tail;
            this.tail.next = node;
            this.tail = node;
        }
    }

    traverse () {
        let currentPoint = this.head;
        while (currentPoint != null) {
            console.log(currentPoint.value + ' <--> ');
            currentPoint = currentPoint.next;
        }
        console.log(null);
    }
};