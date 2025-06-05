import { Component } from '@angular/core';
import { Popover } from 'primeng/popover';
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'dialog-headless-demo',
    templateUrl: './dialog-headless-demo.component.html',
    styleUrls: ['./dialog-headless-demo.component.css'],
    standalone: true,
    imports: [Popover, InputGroup, InputGroupAddonModule, ButtonModule, InputTextModule, CommonModule]
})
export class DialogHeadlessDemo { members = [
        { name: 'Amy Elsner', image: 'amyelsner.png', email: 'amy@email.com', role: 'Owner' },
        { name: 'Bernardo Dominic', image: 'bernardodominic.png', email: 'bernardo@email.com', role: 'Editor' },
        { name: 'Ioni Bowcher', image: 'ionibowcher.png', email: 'ioni@email.com', role: 'Viewer' }
    ];
}