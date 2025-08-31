import pygame
from .entity import Entity
from .projectile import Projectile
import settings

class SpaceShip(Entity):
    def __init__(self, screen_width, screen_height, all_sprites_group, size):
        super().__init__()
        width, height = int(size[0]), int(size[1])
        self.image = pygame.Surface((width, height), pygame.SRCALPHA)
        
        points = [(width / 2, 0), (0, height), (width, height)]
        pygame.draw.polygon(self.image, settings.YELLOW, points)
        
        self.rect = self.image.get_rect(midbottom=(screen_width / 2, screen_height - 10))
        
        self.speed = 5
        self.screen_width = screen_width
        self.screen_height = screen_height
        self.projectiles = pygame.sprite.Group()
        self.all_sprites = all_sprites_group
        
        self.last_shot_time = 0
        self.shot_cooldown = 200

    def update(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_d] or keys[pygame.K_RIGHT]:
            self.rect.x += self.speed
        if keys[pygame.K_a] or keys[pygame.K_LEFT]:
            self.rect.x -= self.speed
        
        if self.rect.right > self.screen_width:
            self.rect.right = self.screen_width
        if self.rect.left < 0:
            self.rect.left = 0

        if keys[pygame.K_SPACE]:
            self.shoot()
            
        self.projectiles.update()

    def shoot(self):
        now = pygame.time.get_ticks()
        if now - self.last_shot_time > self.shot_cooldown:
            self.last_shot_time = now
            projectile = Projectile(self.rect.centerx, self.rect.top, -10, settings.YELLOW, self.screen_height)
            self.projectiles.add(projectile)
            self.all_sprites.add(projectile)